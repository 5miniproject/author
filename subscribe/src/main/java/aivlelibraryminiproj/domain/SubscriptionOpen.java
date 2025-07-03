package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "subscription_open_table")
@Data
public class SubscriptionOpen {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long subscriberId;
    private Long bookId;
    private String title;
}
