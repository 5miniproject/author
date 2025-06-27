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
public class BookScriptsOpenViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private BookScriptsOpenRepository bookScriptsOpenRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenScriptCreated_then_CREATE_1(
        @Payload ScriptCreated scriptCreated
    ) {
        try {
            if (!scriptCreated.validate()) return;

            // view 객체 생성
            BookScriptsOpen bookScriptsOpen = new BookScriptsOpen();
            // view 객체에 이벤트의 Value 를 set 함
            bookScriptsOpen.setId(scriptCreated.getId());
            bookScriptsOpen.setAuthorId(scriptCreated.getAuthorId());
            bookScriptsOpen.setContents(scriptCreated.getContents());
            bookScriptsOpen.setStatus(scriptCreated.getStatus());
            bookScriptsOpen.setCreatedAt(scriptCreated.getCreatedAt());
            bookScriptsOpen.setUpdateAt(scriptCreated.getUpdatedAt());
            // view 레파지 토리에 save
            bookScriptsOpenRepository.save(bookScriptsOpen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenScriptEdited_then_UPDATE_1(
        @Payload ScriptEdited scriptEdited
    ) {
        try {
            if (!scriptEdited.validate()) return;
            // view 객체 조회
            Optional<BookScriptsOpen> bookScriptsOpenOptional = bookScriptsOpenRepository.findById(
                scriptEdited.getId()
            );

            if (bookScriptsOpenOptional.isPresent()) {
                BookScriptsOpen bookScriptsOpen = bookScriptsOpenOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                bookScriptsOpen.setTitle(scriptEdited.getTitle());
                bookScriptsOpen.setContents(scriptEdited.getContents());
                bookScriptsOpen.setUpdateAt(scriptEdited.getUpdatedAt());
                // view 레파지 토리에 save
                bookScriptsOpenRepository.save(bookScriptsOpen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenScriptDeleted_then_DELETE_1(
        @Payload ScriptDeleted scriptDeleted
    ) {
        try {
            if (!scriptDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            bookScriptsOpenRepository.deleteById(scriptDeleted.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}