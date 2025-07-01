package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class SubscriptionPurchased extends AbstractEvent {

    private Boolean isPurchased;
    private Date purchaseDate;
    private Long id;
}
