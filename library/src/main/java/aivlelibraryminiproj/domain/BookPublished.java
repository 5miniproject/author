package aivlelibraryminiproj.domain;

// import java.util.*;
// import javax.persistence.Enumerated;
// import aivlelibraryminiproj.domain.*;
// import aivlelibraryminiproj.domain.Book.BookStatus;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.*;

// DTO 같은 거임, 내가(library 입장에서) 필요한 정보만 담으면 됨
@Data
@ToString
public class BookPublished extends AbstractEvent {
    private Long id;
    private Long authorId;
    private String authorname;
    private String title;
    private String contents;
    private String plot;
    private String plotUrl;
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
    // private String status;
}
