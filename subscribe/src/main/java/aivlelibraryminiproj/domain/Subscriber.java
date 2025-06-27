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
        System.out.println(
            "##### Subscriber.subscriptionRecommend (Policy) called for subscriberId: " + bookSubscriptionFailed.getSubscriberId() + " #####"
        );

        repository().findById(bookSubscriptionFailed.getSubscriberId()).ifPresent(subscriber -> {
            // "포인트 부족" 상황에 대한 메시지
            String recommendationMessage = "포인트가 부족합니다. 구독권 가입 페이지로 이동하여 포인트를 충전하세요.";
            subscriber.setNotification(recommendationMessage); // notification 필드에 알림 메시지 저장
            
            // 2. 변경된 Subscriber 인스턴스 저장
            repository().save(subscriber);
            System.out.println("Subscriber " + subscriber.getId() + " notification and isKt attributes updated and saved.");
            System.out.println("New Notification: '" + subscriber.getNotification() + "', New isKt: " + subscriber.getIsKt());
        });
    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root