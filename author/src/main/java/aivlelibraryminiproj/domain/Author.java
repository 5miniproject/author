package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.AuthorApplication;
import aivlelibraryminiproj.domain.AuthorDeleted;
import aivlelibraryminiproj.domain.AuthorRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String name;

    private String detail;

    private String portfolio;

    private Boolean isApprove;

    @PostPersist
    public void onPostPersist() {
        AuthorRegistered authorRegistered = new AuthorRegistered(this);
        authorRegistered.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove() {
        AuthorDeleted authorDeleted = new AuthorDeleted(this);
        authorDeleted.publishAfterCommit();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AuthorApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }

    //<<< Clean Arch / Port Method
    public void approveAuthor(ApproveAuthorCommand approveAuthorCommand) {
        // 1. 상태를 먼저 변경
        this.setIsApprove(true);

        // 2. 변경된 상태를 담아 이벤트를 생성 및 발행
        AuthorApproved authorApproved = new AuthorApproved(this);
        authorApproved.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void rejectAuthor(RejectAuthorCommand rejectAuthorCommand) {
        // 1. 상태를 먼저 변경
        this.setIsApprove(false);

        // 2. 변경된 상태를 담아 이벤트를 생성 및 발행
        AuthorRejected authorRejected = new AuthorRejected(this);
        authorRejected.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
