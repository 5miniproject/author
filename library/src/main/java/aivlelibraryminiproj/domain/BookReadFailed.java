package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookReadFailed extends AbstractEvent {

    private Long id;

    public BookReadFailed(Book aggregate) {
        super(aggregate);
    }

    public BookReadFailed() {
        super();
    }
}
//>>> DDD / Domain Event
