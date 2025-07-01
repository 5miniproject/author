package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class BookSubscriptionFailed extends AbstractEvent {

    private Long id;
    private Long subscriberId;
    private Long bookId;
    private String title;
}