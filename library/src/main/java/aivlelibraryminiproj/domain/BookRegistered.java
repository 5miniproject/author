package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRegistered extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Long publicationId;
    private String authorName;
    private String title;
    private String contents;
    private String plot;
    private String plotUrl;
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
    
    private Long views;
    private Long subscriptionCount;
    private Boolean isBestSeller;

    private String status;

    public BookRegistered(Book aggregate) {
        super(aggregate);
        this.setStatus(aggregate.getStatus().name());
    }

    public BookRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
