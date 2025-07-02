package aivlelibraryminiproj.domain;

import java.util.*;
import aivlelibraryminiproj.domain.*;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class BookSubscriptionFailed extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long bookId;
    private String title;
}
