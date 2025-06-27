package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private String detail;
    private String portfolio;
    private Boolean isApprove;

    public AuthorRegistered(Author aggregate) {
        super(aggregate);
    }

    public AuthorRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
