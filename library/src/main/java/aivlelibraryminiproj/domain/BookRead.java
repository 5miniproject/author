package aivlelibraryminiproj.domain;

// import aivlelibraryminiproj.domain.*;
// import java.time.LocalDate;
// import java.util.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;


//<<< DDD / Domain Event
@Data
@ToString
public class BookRead extends AbstractEvent {
    private Long subscriberId;
    // private Long views;

    public BookRead(Book aggregate) {
        super(aggregate);
    }

    public BookRead() {
        super();
    }
}
//>>> DDD / Domain Event
