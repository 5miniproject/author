package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.SubscribeApplication;
import aivlelibraryminiproj.domain.SubscriberRegistered;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Subscriber_table")
@Data
//<<< DDD / Aggregate Root
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String name;

    private Boolean isPurchased;

    private Date registerDate;

    private Date purchaseDate;

    private String notification;

    private Boolean isKt;

    @PostPersist
    public void onPostPersist() {
        SubscriberRegistered subscriberRegistered = new SubscriberRegistered(
            this
        );
        subscriberRegistered.publishAfterCommit();
    }

    public static SubscriberRepository repository() {
        SubscriberRepository subscriberRepository = SubscribeApplication.applicationContext.getBean(
            SubscriberRepository.class
        );
        return subscriberRepository;
    }

    //<<< Clean Arch / Port Method
    public void purchaseSubscription(
        PurchaseSubscriptionCommand purchaseSubscriptionCommand
    ) {
        //implement business logic here:

        SubscriptionPurchased subscriptionPurchased = new SubscriptionPurchased(
            this
        );
        subscriptionPurchased.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void subscriptionRecommend(
        BookSubscriptionFailed bookSubscriptionFailed
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Subscriber subscriber = new Subscriber();
        repository().save(subscriber);

        */

        /** Example 2:  finding and process
        

        repository().findById(bookSubscriptionFailed.get???()).ifPresent(subscriber->{
            
            subscriber // do something
            repository().save(subscriber);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
