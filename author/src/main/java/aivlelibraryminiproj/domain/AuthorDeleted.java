package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorDeleted extends AbstractEvent {

    private Long id;

    public AuthorDeleted(Author aggregate) {
        super(aggregate);
    }

    public AuthorDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
