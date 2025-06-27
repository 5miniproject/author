package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookSubscriptionFailed extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long bookId;
    private String title;

    public BookSubscriptionFailed(SubscribeBook aggregate) {
        super(aggregate);
    }

    public BookSubscriptionFailed() {
        super();
    }
}
//>>> DDD / Domain Event
