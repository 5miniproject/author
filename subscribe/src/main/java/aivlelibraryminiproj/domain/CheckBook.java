package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "check_book_table")
@Data
public class CheckBook {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String title;
    
    private Long authorId;

    private Integer subscriptionFee;

    private Boolean isBestSeller;
}
