package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "ebook_statistics_view_table")
@Data
public class EbookStatisticsView {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long bookId;
    private Long subscriptionCount;
    private Long authorId;
    private Boolean isBestSeller;
    private String coverImageUrl;
}
