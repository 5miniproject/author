package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.ScriptApplication;
import aivlelibraryminiproj.domain.ScriptCreated;
import aivlelibraryminiproj.domain.ScriptDeleted;
import aivlelibraryminiproj.domain.ScriptEdited;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BookScript_table")
@Data
//<<< DDD / Aggregate Root
public class BookScript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private String contents;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private String title;

    private String authorname;

    @PostPersist
    public void onPostPersist() {
        ScriptCreated scriptCreated = new ScriptCreated(this);
        scriptCreated.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        ScriptEdited scriptEdited = new ScriptEdited(this);
        scriptEdited.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove() {
        ScriptDeleted scriptDeleted = new ScriptDeleted(this);
        scriptDeleted.publishAfterCommit();
    }

    public static BookScriptRepository repository() {
        BookScriptRepository bookScriptRepository = ScriptApplication.applicationContext.getBean(
            BookScriptRepository.class
        );
        return bookScriptRepository;
    }

    //<<< Clean Arch / Port Method
    public void scriptPublishRequest(
        ScriptPublishRequestCommand scriptPublishRequestCommand
    ) {
        //implement business logic here:

        BookPublishRequested bookPublishRequested = new BookPublishRequested(
            this
        );
        bookPublishRequested.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
