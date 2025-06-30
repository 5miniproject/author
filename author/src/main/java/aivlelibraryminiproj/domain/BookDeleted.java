package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class BookDeleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private Long publicationId;
    private String contents;
    private String coverImageURL;
    private String plot;
    private Long views;
    private String status;
    private String category;
    private Integer subscriptionFee;
    private String plotURL;
    private Boolean isBest;
    private String title;
    private String authorName;
}
