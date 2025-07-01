package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookSubscriptionApplied extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long bookId;
    private Boolean isSubscribed;
    private Date subscriptionDate;
    private Date subscriptionExpiredDate;
    private String title;
    private Integer subscriptionFee;
    private Boolean isBestSeller;
    
    public BookSubscriptionApplied(SubscribeBook aggregate) {
        super(aggregate);
    }

    public BookSubscriptionApplied() {
        super();
    }
}
//>>> DDD / Domain Event
