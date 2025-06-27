package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookDeleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Long publicationId;
    private String contents;
    private String coverImageUrl;
    private String plot;
    private Long views;
    private String status;
    private String category;
    private Integer subscriptionFee;
    private String plotUrl;
    private Boolean isBest;
    private String title;
    private String authorName;

    public BookDeleted(Book aggregate) {
        super(aggregate);
    }

    public BookDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
