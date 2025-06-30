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

import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "BookScript_table")
@Data
//<<< DDD / Aggregate Root
public class BookScript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    @Lob
    @Column
    private String contents;
    
    private String status;

    private Date createdAt;

    private Date updatedAt;

    private String title;

    private String authorname;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = new Date();    // 등록 시 현재 시각으로 생성
        this.updatedAt = new Date();    // 등록 시에도 수정일 초기화
        this.status = "DRAFT";          // 상태도 초기화
    }

    @PostPersist
    public void onPostPersist() {
        ScriptCreated scriptCreated = new ScriptCreated(this);
        scriptCreated.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        if(this.getId() == null){
            throw new IllegalStateException("원고가 없습니다.");
        }
        this.updatedAt = new Date();    // 수정 시마다 현재 시각으로 갱신
    }

    @PostUpdate
    public void onPostUpdate() {
        if ("DRAFT".equals(this.status)) {
            ScriptEdited scriptEdited = new ScriptEdited(this);
            scriptEdited.publishAfterCommit();
        }
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
        if("DRAFT".equals(this.status)){
            //implement business logic here:
            BookPublishRequested bookPublishRequested = new BookPublishRequested(
                this
            );
            bookPublishRequested.publishAfterCommit();
        }
        else {
            throw new IllegalStateException("출간 요청 이후에는 원고를 다시 출간할 수 없습니다.");
        }
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
