package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PointShorted extends AbstractEvent {

    private Long userId;
    private Integer point;

    public PointShorted(Point aggregate) {
        super(aggregate);
    }

    public PointShorted() {
        super();
    }
}
//>>> DDD / Domain Event
