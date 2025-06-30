package aivlelibraryminiproj.domain;

// import aivlelibraryminiproj.domain.*;
// import java.util.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BestSellerArchived extends AbstractEvent {
    private Long bookId;
    private Boolean isBestSeller;

    public BestSellerArchived(Book aggregate) {
        super(aggregate);
    }

    public BestSellerArchived() {
        super();
    }
}
//>>> DDD / Domain Event
