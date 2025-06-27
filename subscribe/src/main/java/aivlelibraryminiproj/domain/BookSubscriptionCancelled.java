package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookSubscriptionCancelled extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long authorId;
    private Long bookId;
    private Boolean isSubscribed;
    private String status;
    private Date subscriptionDate;
    private Date subscriptionExpiredDate;

    public BookSubscriptionCancelled(SubscribeBook aggregate) {
        super(aggregate);
    }

    public BookSubscriptionCancelled() {
        super();
    }
}
//>>> DDD / Domain Event
