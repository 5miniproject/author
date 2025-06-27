package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriberRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private Boolean isPurchased;
    private Date registerDate;
    private Date purchaseDate;
    private String notification;
    private Boolean isKt;

    public SubscriberRegistered(Subscriber aggregate) {
        super(aggregate);
    }

    public SubscriberRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
