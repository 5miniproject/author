package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ScriptDeleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String title;
    private String authorname;

    public ScriptDeleted(BookScript aggregate) {
        super(aggregate);
    }

    public ScriptDeleted() {
        super();
    }
}
//>>> DDD / Domain Event
