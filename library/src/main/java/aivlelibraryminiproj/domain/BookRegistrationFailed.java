package aivlelibraryminiproj.domain;

// import aivlelibraryminiproj.domain.*;
// import java.time.LocalDate;
// import java.util.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRegistrationFailed extends AbstractEvent {

    // private Long id;
    // private Long authorId;
    
    // private String contents;
    // private String coverImageUrl;
    // private String plot;
    // private String status;
    // private String category;
    // private Integer subscriptionFee;
    // private String plotUrl;
    // private Long views;
    // private Boolean isBest;
    // private String title;
    // private String authorName;

    private Long publicationId;
    private String reason;

    public BookRegistrationFailed(Book aggregate) {
        super(aggregate);
    }

    public BookRegistrationFailed() {
        super();
    }
}
//>>> DDD / Domain Event
