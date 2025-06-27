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
    BookRepository bookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookPublished'"
    )
    public void wheneverBookPublished_BookPublish(
        @Payload BookPublished bookPublished
    ) {
        BookPublished event = bookPublished;
        System.out.println(
            "\n\n##### listener BookPublish : " + bookPublished + "\n\n"
        );

        // Sample Logic //
        Book.bookPublish(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookRead'"
    )
    public void wheneverBookRead_ArchiveBestseller(@Payload BookRead bookRead) {
        BookRead event = bookRead;
        System.out.println(
            "\n\n##### listener ArchiveBestseller : " + bookRead + "\n\n"
        );

        // Comments //
        //When 조회수가 5 이상이 되면,
        // Then 베스트 셀러로 선정

        // Sample Logic //
        Book.archiveBestseller(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
