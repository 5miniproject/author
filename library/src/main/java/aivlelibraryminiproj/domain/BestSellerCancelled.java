package aivlelibraryminiproj.domain;

// import aivlelibraryminiproj.domain.*;
// import java.util.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BestSellerCancelled extends AbstractEvent {
    private Long bookId;
    private Boolean isBestSeller;

    public BestSellerCancelled(Book aggregate) {
        super(aggregate);
    }

    public BestSellerCancelled() {
        super();
    }
}
//>>> DDD / Domain Event
