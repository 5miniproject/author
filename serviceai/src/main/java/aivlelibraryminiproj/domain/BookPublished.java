package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookPublished extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String coverImageUrl;
    private String plot;
    private String status;
    private String plotUrl;
    private String category;
    private Integer subscriptionFee;
    private String title;
    private String authorname;

    public BookPublished(Publication aggregate) {
        super(aggregate);
    }

    public BookPublished() {
        super();
    }
}
//>>> DDD / Domain Event
