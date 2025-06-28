package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
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
