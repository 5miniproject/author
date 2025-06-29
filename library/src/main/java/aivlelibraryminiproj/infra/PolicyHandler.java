package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataIntegrityViolationException;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    // @Autowired
    // BookRepository bookRepository;
    
    private final BookRepository bookRepository;
    private final SubscriptionPermissionRepository permissionRepository;

    // @Autowired 생략 가능
    public PolicyHandler(BookRepository bookRepository,
    SubscriptionPermissionRepository permissionRepository) {
        this.bookRepository = bookRepository;
        this.permissionRepository = permissionRepository;
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 1. 책 등록
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookPublished'"
    )
    public void wheneverBookPublished_RegisterBook(
        @Payload BookPublished bookPublished
        ) {
            BookPublished event = bookPublished;
            System.out.println(
                "\n\n##### listener PublishBook : " + bookPublished + "\n\n"
            );

            // 1. Book 애그리거트를 생성하여 비즈니스 로직은 Book 생성자에게 위임
            Book book = new Book(bookPublished);
            // 2. Repository를 통해 애그리거트를 저장
            bookRepository.save(book);
            // 3. 이벤트 발행은 Book 애그리거트에서 PostPersist
        }

    // 2. 책 구독 성공 시 구독 횟수 증가
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDecreased'"
    )
    public void wheneverPointDecreased_UpdateSubscriptionCount(
        @Payload PointDecreased pointDecreased
        ) {
            PointDecreased event = pointDecreased;
            System.out.println(
                "\n\n##### listener UpdateSubscriptionCount: " + pointDecreased + "\n\n"
            );
            
            // 1. bookId 추출
            Long bookId = event.getBookId();
            if (bookId == null) {
                System.err.println("책의 ID를 찾을 수 없습니다.");
                return;
            }
            // 2. 추출한 bookId로 Book 애그리거트 조회
            bookRepository.findById(bookId).ifPresentOrElse(book -> {
                // 3. Book 애그리거트의 메서드 호출
                book.increaseCountAndCheckBestseller();
                // 4. Repository를 이용하여 저장
                bookRepository.save(book);
            }, () -> {
                System.err.println("ID와 일치하는 책을 찾을 수 없습니다.");
            });
        }
    
    // 3. 책 구독 취소 시 구독 횟수 차감
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionCancelled'"
    )
    public void wheneverBookSubscriptionCancelled_UpdateSubscriptionCount(
        @Payload BookSubscriptionCancelled event
    ) {
        System.out.println(
            "\n\n##### listener UpdateSubscriptionCountOnCancel : " + event + "\n\n"
        );

        Long bookId = event.getBookId();
        if (bookId == null) {
            System.err.println("책의 ID를 찾을 수 없습니다.");
            return;
        }
        bookRepository.findById(bookId).ifPresentOrElse(book -> {
            // Book 애그리거트의 구독 취소 로직 호출
            book.decreaseCountAndCheckBestseller();
            bookRepository.save(book);
        }, () -> {
            System.err.println("ID와 일치하는 책을 찾을 수 없습니다.");
        });
    }

    // 4. 구독 성공 시 권한 생성
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDecreased'"
    )
    public void wheneverPointDecreased_CreatePermission(
        @Payload PointDecreased event
    ) {
        if (event.getBookId() != null) {
            try {
                System.out.println("\n\n##### listener CreatePermission : " + event + "\n\n");
                // 1. 복합 키 생성
                SubscriptionPermissionId permissionId = new SubscriptionPermissionId(
                    event.getSubscriberId(),
                    event.getBookId()
                );
                
                // 2. 리드 모델 엔티티 생성
                SubscriptionPermission permission = new SubscriptionPermission(permissionId);
                
                // 3. 리드 모델 DB에 저장
                permissionRepository.save(permission);

                System.out.println("\n\n##### Permission Created: " + permissionId + "\n\n");
            } catch (DataIntegrityViolationException e) {
                // 데이터베이스의 Primary Key 또는 Unique 제약 조건 위반 시 이 예외가 발생합니다.
                // 즉, 이미 구독 권한이 존재한다는 의미입니다.
                System.err.println(
                    "\n\n##### Duplicate subscription attempt detected and ignored for: " + 
                    "SubscriberId=" + event.getSubscriberId() + 
                    ", BookId=" + event.getBookId() + "\n\n"
                );
                // 이 상황을 모니터링 시스템에 로그로 남기거나,
                // 필요한 경우 "중복 구독 시도 감지"와 같은 별도의 이벤트를 발행하여
                // 비정상적인 흐름을 추적할 수도 있습니다.
            }
        }
    }

    // 두 이벤트를 포괄하는 인터페이스나 공통 상위 클래스 필요
    public interface BookSubscriptionEnded {
        Long getSubscriberId();
        Long getBookId();
    }

    // 5. 구독 취소 시 권한 삭제 (실패는 딱히?)
    @StreamListener(
        value = KafkaProcessor.INPUT,
        // condition = "headers['type']=='BookSubscriptionCancelled'"
        condition = "headers['type']=='BookSubscriptionCancelled' || headers['type']=='BookSubscriptionFailed'"
    )
    public void wheneverSubscriptionEnded_DeletePermission(
        @Payload BookSubscriptionEnded event 
    ) {
        System.out.println(
            "\n\n##### listener DeletePermission : " + event + "\n\n"
        );

        if (event.getBookId() == null || event.getSubscriberId() == null) {
            return;
        }
        
        SubscriptionPermissionId permissionId = new SubscriptionPermissionId(
            event.getSubscriberId(),
            event.getBookId()
        );

        try {
            permissionRepository.deleteById(permissionId);
            System.out.println("##### Permission Deleted: " + permissionId);
        } catch (Exception e) {
            System.err.println("##### Failed to delete permission: " + permissionId + " - " + e.getMessage());
        }
    }

}
//>>> Clean Arch / Inbound Adaptor
