package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ScriptEdited extends AbstractEvent {

    private Long id;
    private Date updatedAt;
    private String title;
    private Long authorId;
    private String contents;

    public ScriptEdited(BookScript aggregate) {
        super(aggregate);
    }

    public ScriptEdited() {
        super();
    }
}
//>>> DDD / Domain Event
