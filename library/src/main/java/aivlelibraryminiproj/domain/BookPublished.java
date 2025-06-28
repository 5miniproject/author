package aivlelibraryminiproj.domain;

import java.util.*;
import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class BookPublished extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String authorName;
    private String title;
    private String contents;
    private String plot;
    private String plotUrl;
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
}
