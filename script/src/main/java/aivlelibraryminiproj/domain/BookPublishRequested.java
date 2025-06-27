package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookPublishRequested extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private String authorname;
    private String title;

    public BookPublishRequested(BookScript aggregate) {
        super(aggregate);
    }

    public BookPublishRequested() {
        super();
    }
}
//>>> DDD / Domain Event
