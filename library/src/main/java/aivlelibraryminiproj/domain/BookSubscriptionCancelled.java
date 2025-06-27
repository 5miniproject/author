package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class BookSubscriptionCancelled extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long authorId;
    private Long bookId;
    private Boolean isSubscribed;
    private String status;
    private Date subscriptionDate;
    private Date subscriptionExpiredDate;
}
