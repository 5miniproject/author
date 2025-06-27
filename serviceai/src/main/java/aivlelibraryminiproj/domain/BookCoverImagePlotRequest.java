package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookCoverImagePlotRequest extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Long scriptId;
    private String contents;
    private String title;

    public BookCoverImagePlotRequest(Publication aggregate) {
        super(aggregate);
    }

    public BookCoverImagePlotRequest() {
        super();
    }
}
//>>> DDD / Domain Event
