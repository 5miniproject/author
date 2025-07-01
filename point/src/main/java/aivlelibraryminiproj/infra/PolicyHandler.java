package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
            "\n\n##### listener PointAdd : " +
            subscriberRegistered +
            "\n\n"
        );

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
            "\n\n##### listener PointUse : " +
            bookSubscriptionApplied +
            "\n\n"
        );

        // Sample Logic //
        Point.pointUse(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionPurchased'"
    )
    public void wheneverSubscriptionPurchased_SubscriptionPurchased(
        @Payload SubscriptionPurchased subscriptionPurchased
    ) {
        SubscriptionPurchased event = subscriptionPurchased;
        System.out.println(
            "\n\n##### listener SubscriptionPurchased : " +
            subscriptionPurchased +
            "\n\n"
        );

        // Sample Logic //
        Point.subscriptionPurchased(event);
    }

}
