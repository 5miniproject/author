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
    SubscriberRepository subscriberRepository;

    @Autowired
    SubscribeBookRepository subscribeBookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionFailed'"
    )
    public void wheneverBookSubscriptionFailed_SubscriptionRecommend(
        @Payload BookSubscriptionFailed bookSubscriptionFailed
    ) {
        BookSubscriptionFailed event = bookSubscriptionFailed;
        System.out.println(
            "\n\n##### listener SubscriptionRecommend : " +
            bookSubscriptionFailed +
            "\n\n"
        );

        // Sample Logic //
        Subscriber.subscriptionRecommend(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointShorted'"
    )
    public void wheneverPointShorted_BookSubscriptionFail(
        @Payload PointShorted pointShorted
    ) {
        PointShorted event = pointShorted;
        System.out.println(
            "\n\n##### listener BookSubscriptionFail : " + pointShorted + "\n\n"
        );

        // Sample Logic //
        SubscribeBook.bookSubscriptionFail(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
