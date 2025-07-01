package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "BookScriptsOpen_table")
@Data
public class BookScriptsOpen {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long authorId;
    @Lob
    @Column
    private String contents;
    private String status;
    private Date createdAt;
    private Date updateAt;
    private String title;
}
