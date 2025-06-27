package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ScriptCreated extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private String title;
    private Date createdAt;
    private Date updatedAt;

    public ScriptCreated(BookScript aggregate) {
        super(aggregate);
    }

    public ScriptCreated() {
        super();
    }
}
//>>> DDD / Domain Event
