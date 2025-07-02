package aivlelibraryminiproj.infra;

// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import javax.naming.NameParser;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.dao.DataIntegrityViolationException;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;


//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    BookRepository bookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookPublished'"
    )
    public void wheneverBookPublished_PublishBook(
        @Payload BookPublished bookPublished
    ) {
        BookPublished event = bookPublished;
        System.out.println(
            "\n\n##### listener PublishBook : " +
            bookPublished +
            "\n\n"
        );

        Book.publishBook(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionApplied'"
    )
    public void wheneverBookSubscriptionApplied_ArchiveBestseller(
        @Payload BookSubscriptionApplied bookSubscriptionApplied
    ) {
        BookSubscriptionApplied event = bookSubscriptionApplied;
        System.out.println(
            "\n\n##### listener ArchiveBestseller : " +
            bookSubscriptionApplied +
            "\n\n"
        );

        Book.archiveBestseller(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionCancelled'"
    )
    public void wheneverBookSubscriptionCancelled_CancelBestSeller(
        @Payload BookSubscriptionCancelled bookSubscriptionCancelled
    ) {
        BookSubscriptionCancelled event = bookSubscriptionCancelled;
        System.out.println(
            "\n\n##### listener CancelBestSeller : " +
            bookSubscriptionCancelled +
            "\n\n"
        );

        Book.cancelBestSeller(event.getBookId());
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionFailed'"
    )
    public void wheneverBookSubscriptionFailed_CancelBestSeller(
        @Payload BookSubscriptionFailed bookSubscriptionFailed
    ) {
        BookSubscriptionFailed event = bookSubscriptionFailed;
        System.out.println(
            "\n\n##### listener CancelBestSeller : " +
            bookSubscriptionFailed +
            "\n\n"
        );

        Book.cancelBestSeller(event.getBookId());
    }

}
//>>> Clean Arch / Inbound Adaptor
