package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

/**
 * '구독 권한' 리드 모델 생성에 실패했을 때 발행되는 이벤트.
 * 이 이벤트는 Point BC가 '포인트 환불' 보상 트랜잭션을 수행하도록 트리거합니다.
 */
@Data
@ToString
public class SubscriptionPermissionFailed extends AbstractEvent {

    private Long subscriberId; 
    private Long bookId; 
    private Integer amountToRefund; // PointDecreased 이벤트로부터 전달받아야 함
    private String reason;

    public SubscriptionPermissionFailed() {
        super();
    }
}
