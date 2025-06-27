package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class BookPublished extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String contents;
    private String coverImageURL;
    private String plot;
    private String status;
    private String plotURL;
    private String category;
    private Integer subscriptionFee;
    private String title;
    private String authorname;
}