package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.ServiceaiApplication;
import aivlelibraryminiproj.domain.BookCoverImagePlotRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Publication_table")
@Data
//<<< DDD / Aggregate Root
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private Long scriptId;

    private String title;

    private String contents;

    private String coverImageUrl;

    private String plot;

    private String status;

    private String plotUrl;

    private String category;

    private Integer subscriptionFee;

    private String authorname;

    public static PublicationRepository repository() {
        PublicationRepository publicationRepository = ServiceaiApplication.applicationContext.getBean(
            PublicationRepository.class
        );
        return publicationRepository;
    }

    //<<< Clean Arch / Port Method
    public void publishBook(PublishBookCommand publishBookCommand) {
        //implement business logic here:

        BookPublished bookPublished = new BookPublished(this);
        bookPublished.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void regenerateI(RegenerateICommand regenerateICommand) {
        //implement business logic here:

        RegenerationRequested regenerationRequested = new RegenerationRequested(
            this
        );
        regenerationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void publicationRegistration(
        BookPublishRequested bookPublishRequested
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Publication publication = new Publication();
        repository().save(publication);

        BookCoverImagePlotRequest bookCoverImagePlotRequest = new BookCoverImagePlotRequest(publication);
        bookCoverImagePlotRequest.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookPublishRequested.get???()).ifPresent(publication->{
            
            publication // do something
            repository().save(publication);

            BookCoverImagePlotRequest bookCoverImagePlotRequest = new BookCoverImagePlotRequest(publication);
            bookCoverImagePlotRequest.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
