package aivlelibraryminiproj.domain;

import java.util.*;
import aivlelibraryminiproj.domain.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

// DTO 같은 거임, 내가(library 입장에서) 필요한 정보만 담으면 됨
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
    private Integer subscriptionFee;
    private Boolean isBestSeller;
}
