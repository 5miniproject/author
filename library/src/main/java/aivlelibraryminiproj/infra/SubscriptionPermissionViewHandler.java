package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * SubscriptionPermission 리드 모델을 관리하는 전용 핸들러 (뷰 핸들러)
 * 조회용 권한 데이터만 다루기 위해 PolicyHandler에서 분리
 */
@Service
@Transactional
public class SubscriptionPermissionViewHandler {

    // @Autowired
    // private SubscriptionPermissionRepository permissionRepository;

    private final SubscriptionPermissionRepository permissionRepository;

    @Autowired
    public SubscriptionPermissionViewHandler(SubscriptionPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
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
            } catch (Exception e) {
                // 예상치 못한 오류로 구독 권한 생성 실패 시 포인트 환불을 위한 보상 트랜잭션
                System.err.println(
                    "\n\n##### Failed to create permission due to system error for: " +
                    "SubscriberId=" + event.getSubscriberId() + ", BookId=" + event.getBookId() + "\n\n"
                );

                // 1. Saga 실패 이벤트를 생성
                SubscriptionPermissionFailed failedEvent = new SubscriptionPermissionFailed();
                failedEvent.setSubscriberId(event.getSubscriberId());
                failedEvent.setBookId(event.getBookId());
                failedEvent.setAmountToRefund(event.getPoint()); 
                failedEvent.setReason(e.getMessage());

                // 2. 실패 이벤트를 발행하여 Point BC의 보상 트랜잭션을 호출
                failedEvent.publishAfterCommit();
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