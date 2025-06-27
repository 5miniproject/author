package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PublishBookCommand {

    private Long id;
    private Long authorId;
    private String contents;
    private String coverImageUrl;
    private String plot;
    private String status;
    private String plotUrl;
    private String category;
    private Integer subscriptionFee;
    private String title;
    private String authorname;
}
