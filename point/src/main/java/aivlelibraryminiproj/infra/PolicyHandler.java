package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * [1] 구독자가 가입되었을 때 → 포인트 적립
     * - KT 사용자는 5000포인트
     * - 그 외는 1000포인트
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
     * [2] 책 구독이 발생했을 때 → 포인트 차감
     * - 차감 기준: 1000포인트
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
            }
        } else {
            System.out.println("포인트 정보 없음: 사용자 ID = " + event.getSubscriberId());
        }
    }
}
