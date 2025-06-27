package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionPurchased extends AbstractEvent {

    private Boolean isPurchased;
    private Date purchaseDate;
    private Long id;

    public SubscriptionPurchased(Subscriber aggregate) {
        super(aggregate);
    }

    public SubscriptionPurchased() {
        super();
    }
}
//>>> DDD / Domain Event
