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


    // ✨✨✨ 여기에 @PrePersist 메소드를 추가합니다. ✨✨✨
    @PrePersist
    public void prePersist() {
        this.registerDate = new Date(); // 현재 시스템의 날짜와 시간을 registerDate에 설정합니다.
        // 필요하다면 다른 필드의 기본값도 여기서 설정할 수 있습니다.
        this.isPurchased = false;
        if (this.isKt == null) {
            this.isKt = false;
        }
    }

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
        // ✨✨✨ 여기에 비즈니스 로직을 구현합니다:
        // PurchaseSubscriptionCommand에서 받은 값을 Subscriber 애그리게이트의 필드에 설정합니다.
        this.setIsPurchased(purchaseSubscriptionCommand.getIsPurchased());
        this.setPurchaseDate(new Date());

        // 애그리게이트의 상태가 업데이트된 후 이벤트를 발행합니다.
        // 이렇게 발행되는 SubscriptionPurchased 이벤트는 업데이트된 'this' 애그리게이트의 상태를 반영할 것입니다.
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