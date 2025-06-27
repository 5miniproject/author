package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "EbookStatisticsView_table")
@Data
public class EbookStatisticsView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String publicationId;
    private Long views;
}
