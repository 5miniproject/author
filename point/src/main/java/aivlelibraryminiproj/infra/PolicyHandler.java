package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    PointRepository pointRepository;

    /**
     * [1] 구독자 가입 → 포인트 적립 (KT: 5000, 그 외: 1000)
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriberRegistered'"
    )
    public void wheneverSubscriberRegistered_PointAdd(
        @Payload SubscriberRegistered event
    ) {
        System.out.println("##### listener PointAdd : " + event);

        // 중복 생성 방지 (이미 포인트 객체가 존재하면 패스)
        if (pointRepository.findByUserId(event.getId()).isPresent()) return;

        Point point = new Point();
        point.setUserId(event.getId());
        point.setIsKT(event.getIsKT());
        point.setPoint(event.getIsKT() ? 5000 : 1000); // KT 여부에 따라 포인트 설정

        pointRepository.save(point);

        PointAdded added = new PointAdded(point);
        added.publishAfterCommit();
    }

    /**
     * [2] 책 구독 발생 → 포인트 차감 (1000포인트)
     * 포인트 부족 시 PointShorted 이벤트 발행
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionApplied'"
    )
    public void wheneverBookSubscriptionApplied_PointUse(
        @Payload BookSubscriptionApplied event
    ) {
        System.out.println("##### listener PointUse : " + event);

        Optional<Point> optionalPoint = pointRepository.findByUserId(event.getSubscriberId());
        if (optionalPoint.isPresent()) {
            Point point = optionalPoint.get();
            int usedPoint = 1000;

            if (point.getPoint() >= usedPoint) {
                point.setPoint(point.getPoint() - usedPoint);
                pointRepository.save(point);

                PointDecreased decreased = new PointDecreased(point);
                decreased.setUsedPoint(usedPoint);
                decreased.publishAfterCommit();
            } else {
                System.out.println("포인트 부족: 사용자 ID = " + point.getUserId());
                // 부족 이벤트 발행 (설계와 다이어그램 반영)
                PointShorted shorted = new PointShorted(point);
                shorted.setUsedPoint(usedPoint);
                shorted.publishAfterCommit();
            }
        } else {
            System.out.println("포인트 정보 없음: 사용자 ID = " + event.getSubscriberId());
        }
    }
}
