package aivlelibraryminiproj.domain;

import java.util.*;
import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

// DTO 같은 거임, 내가(library 입장에서) 필요한 정보만 담으면 됨
@Data
@ToString
public class BookPublished extends AbstractEvent {

    private Long publicationId;
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
