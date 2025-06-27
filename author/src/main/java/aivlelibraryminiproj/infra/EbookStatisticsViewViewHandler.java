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
public class EbookStatisticsViewViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private EbookStatisticsViewRepository ebookStatisticsViewRepository;
    //>>> DDD / CQRS
}
