package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CheckSubsciptionPermissonViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CheckSubsciptionPermissonRepository checkSubsciptionPermissonRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionApplied_then_CREATE_1(
        @Payload BookSubscriptionApplied bookSubscriptionApplied
    ) {
        try {
            if (!bookSubscriptionApplied.validate()) return;

            // view 객체 생성
            CheckSubsciptionPermisson checkSubsciptionPermisson = new CheckSubsciptionPermisson();
            // view 객체에 이벤트의 Value 를 set 함
            checkSubsciptionPermisson.setSubscriberId(
                bookSubscriptionApplied.getSubscriberId()
            );
            checkSubsciptionPermisson.setBookId(
                bookSubscriptionApplied.getBookId()
            );
            checkSubsciptionPermisson.setId(bookSubscriptionApplied.getId());
            // view 레파지 토리에 save
            checkSubsciptionPermissonRepository.save(checkSubsciptionPermisson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionCancelled_then_DELETE_1(
        @Payload BookSubscriptionCancelled bookSubscriptionCancelled
    ) {
        try {
            if (!bookSubscriptionCancelled.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            checkSubsciptionPermissonRepository.deleteById(
                bookSubscriptionCancelled.getId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionFailed_then_DELETE_2(
        @Payload BookSubscriptionFailed bookSubscriptionFailed
    ) {
        try {
            if (!bookSubscriptionFailed.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            checkSubsciptionPermissonRepository.deleteById(
                bookSubscriptionFailed.getId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
