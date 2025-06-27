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

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    PointRepository pointRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriberRegistered'"
    )
    public void wheneverSubscriberRegistered_PointAdd(
        @Payload SubscriberRegistered subscriberRegistered
    ) {
        SubscriberRegistered event = subscriberRegistered;
        System.out.println(
            "\n\n##### listener PointAdd : " + subscriberRegistered + "\n\n"
        );

        // Comments //
        //When SubscriberRegistered,
        // Then Add1000Points
        // When SubscriberUsingKTRegistered,
        // Then Add5000Points
        //

        // Sample Logic //
        Point.pointAdd(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionApplied'"
    )
    public void wheneverBookSubscriptionApplied_PointUse(
        @Payload BookSubscriptionApplied bookSubscriptionApplied
    ) {
        BookSubscriptionApplied event = bookSubscriptionApplied;
        System.out.println(
            "\n\n##### listener PointUse : " + bookSubscriptionApplied + "\n\n"
        );

        // Comments //
        //When 월정액 이용자가 아닌 구독자가 책을 구독하면,
        // Then 포인트가 차감됨

        // Sample Logic //
        Point.pointUse(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
