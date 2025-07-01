package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.SubscribeApplication;
import aivlelibraryminiproj.domain.BookSubscriptionApplied;
import aivlelibraryminiproj.domain.BookSubscriptionCancelled;
import aivlelibraryminiproj.domain.BookSubscriptionFailed;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    
    private String title;

    private Boolean isSubscribed;

    private String status;

    private Date subscriptionDate;
    
    private Date subscriptionExpiredDate;

    @PrePersist
    public void prePersist() {
        // 1. subscriptionDate를 현재 시스템의 날짜와 시간으로 설정
        Instant now = Instant.now(); // UTC 기준 현재 시각을 Instant 객체로 얻습니다.
        this.subscriptionDate = Date.from(now); // Instant를 java.util.Date로 변환하여 설정
        // 2. subscriptionExpiredDate를 subscriptionDate로부터 1년 추가하여 설정
        // Instant에 1년(ChronoUnit.YEARS)을 더한 후 다시 java.util.Date로 변환합니다.
        this.subscriptionExpiredDate = Date.from(now.plus(1, ChronoUnit.YEARS));

        // 3. isSubscribed 필드 초기값 설정
        // 구독 신청 시에는 보통 'true'로 시작합니다.
        // 만약 요청에서 이미 이 값이 넘어왔다면 덮어쓰지 않도록 null 체크를 할 수 있습니다.
        if (this.isSubscribed == null) {
            this.isSubscribed = true;
        }

        // 4. status 필드 초기값 설정
        // 구독 신청 시의 초기 상태를 "APPLIED" 또는 "ACTIVE" 등으로 설정합니다.
        // 이 또한 요청에서 값이 넘어오지 않았을 경우에만 설정하도록 null 체크를 할 수 있습니다.
        if (this.status == null) {
            this.status = "APPLIED"; // 예: 구독 신청이 완료된 초기 상태
        }
    }

    @PostPersist
    public void onPostPersist() {
        BookSubscriptionApplied bookSubscriptionApplied = new BookSubscriptionApplied(
            this
        );
        bookSubscriptionApplied.publishAfterCommit();
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
        // PointShorted 이벤트에서 'subcribeId'를 사용하여 해당 구독을 식별하고 실패 처리합니다.

        Long subscriptionIdToFail = pointShorted.getSubscriptionId(); // PointShorted에서 구독 ID를 가져옴

        // 1. 해당 구독 ID를 가진 SubscribeBook 애그리게이트를 데이터베이스에서 찾습니다.
        repository().findById(subscriptionIdToFail).ifPresent(subscribeBook -> {
            // 2. 찾은 SubscribeBook 애그리게이트의 상태를 '실패'로 업데이트합니다.
            // 예를 들어, status 필드를 "FAILED"로 변경하고 isSubscribed를 false로 설정
            subscribeBook.setStatus("FAILED"); // 상태를 'FAILED'로 설정 (또는 'CANCELLED' 등 적절한 상태)
            subscribeBook.setIsSubscribed(false); // 구독 여부를 false로 변경

            // 3. 변경된 SubscribeBook 애그리게이트를 저장합니다.
            repository().save(subscribeBook);

            // 4. 업데이트된 SubscribeBook 애그리게이트의 정보를 담아 BookSubscriptionFailed 이벤트를 발행합니다.
            // 이렇게 하면 발행되는 실패 이벤트가 정확한 구독 정보(ID, 책 ID, 구독자 ID, 제목 등)를 가지게 됩니다.
            BookSubscriptionFailed bookSubscriptionFailed = new BookSubscriptionFailed(subscribeBook);
            bookSubscriptionFailed.publishAfterCommit();

            System.out.println("##### 구독 ID: " + subscriptionIdToFail + " 에 대한 구독이 포인트 부족으로 인해 실패 처리되었습니다. #####");
        });

        // 만약 PointShorted 이벤트에서 받은 subcribeId에 해당하는 SubscribeBook을 찾지 못했을 경우
        // (예: 이미 삭제되었거나 잘못된 ID일 경우)
        if (repository().findById(subscriptionIdToFail).isEmpty()) {
            System.out.println("##### 경고: PointShorted 이벤트(subcribeId: " + subscriptionIdToFail + ")에 해당하는 SubscribeBook을 찾을 수 없어 실패 처리하지 못했습니다. #####");
            // 이 경우, 예를 들어 로깅하거나, 시스템 관리자에게 알림을 보내는 등의 추가적인 오류 처리 로직을 고려할 수 있습니다.
        }
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
