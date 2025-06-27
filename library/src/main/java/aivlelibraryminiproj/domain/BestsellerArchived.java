package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BestsellerArchived extends AbstractEvent {

    private Boolean isBest;

    public BestsellerArchived(Book aggregate) {
        super(aggregate);
    }

    public BestsellerArchived() {
        super();
    }
}
//>>> DDD / Domain Event
