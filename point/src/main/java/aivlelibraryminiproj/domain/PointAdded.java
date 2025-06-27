package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PointAdded extends AbstractEvent {

    private Long userId;
    private Integer point;
    private Boolean isKt;

    public PointAdded(Point aggregate) {
        super(aggregate);
    }

    public PointAdded() {
        super();
    }
}
//>>> DDD / Domain Event
