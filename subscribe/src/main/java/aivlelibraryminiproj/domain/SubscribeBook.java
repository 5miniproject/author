package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.SubscribeApplication;
import aivlelibraryminiproj.domain.BookSubscriptionApplied;
import aivlelibraryminiproj.domain.BookSubscriptionCancelled;
import aivlelibraryminiproj.domain.BookSubscriptionFailed;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SubscribeBook_table")
@Data
//<<< DDD / Aggregate Root
public class SubscribeBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long subscriberId;

    private Long authorId;

    private Long bookId;

    private Boolean isSubscribed;

    private String status;

    private Date subscriptionDate;

    private Date subscriptionExpiredDate;

    private String title;

    @PostPersist
    public void onPostPersist() {
        BookSubscriptionApplied bookSubscriptionApplied = new BookSubscriptionApplied(
            this
        );
        bookSubscriptionApplied.publishAfterCommit();

        BookSubscriptionFailed bookSubscriptionFailed = new BookSubscriptionFailed(
            this
        );
        bookSubscriptionFailed.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove() {
        BookSubscriptionCancelled bookSubscriptionCancelled = new BookSubscriptionCancelled(
            this
        );
        bookSubscriptionCancelled.publishAfterCommit();
    }

    public static SubscribeBookRepository repository() {
        SubscribeBookRepository subscribeBookRepository = SubscribeApplication.applicationContext.getBean(
            SubscribeBookRepository.class
        );
        return subscribeBookRepository;
    }

    //<<< Clean Arch / Port Method
    public static void bookSubscriptionFail(PointShorted pointShorted) {
        //implement business logic here:

        /** Example 1:  new item 
        SubscribeBook subscribeBook = new SubscribeBook();
        repository().save(subscribeBook);

        BookSubscriptionFailed bookSubscriptionFailed = new BookSubscriptionFailed(subscribeBook);
        bookSubscriptionFailed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(pointShorted.get???()).ifPresent(subscribeBook->{
            
            subscribeBook // do something
            repository().save(subscribeBook);

            BookSubscriptionFailed bookSubscriptionFailed = new BookSubscriptionFailed(subscribeBook);
            bookSubscriptionFailed.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
