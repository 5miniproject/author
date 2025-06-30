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

    // @Autowired 생략 가능
    public PolicyHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 1. 책 등록
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookPublished'"
    )
    public void wheneverBookPublished_RegisterBook(
        @Payload BookPublished event
        ) {
            try {
                // BookPublished event = bookPublished;
                System.out.println(
                    "\n\n##### listener PublishBook : " + event + "\n\n"
                );

                // 1. Book 애그리거트를 생성하여 비즈니스 로직은 Book 생성자에게 위임
                // Book book = new Book(bookPublished);
                Book book = new Book(event);
                // 2. Repository를 통해 애그리거트를 저장
                bookRepository.save(book);
                // 3. 이벤트 발행은 Book 애그리거트에서 PostPersist
            } catch (Exception e) { // serviceai에서 최종 출간 요청했지만 library에서 예상치 못한 이유로 Book 생성에 실패했을 때 보상 트랜잭션
                System.err.println("\n\n##### Failed to register book with publication ID: " + event.getId());

                BookRegistrationFailed failedEvent = new BookRegistrationFailed();
                failedEvent.setPublicationId(event.getId());
                failedEvent.setReason(e.getMessage());

                failedEvent.publishAfterCommit();
            }
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
}
//>>> Clean Arch / Inbound Adaptor
