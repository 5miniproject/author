package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.LibraryApplication;
import aivlelibraryminiproj.domain.BestsellerArchived;
import aivlelibraryminiproj.domain.BookDeleted;
import aivlelibraryminiproj.domain.BookPublished;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private Long publicationId;

    private String contents;

    private String coverImageUrl;

    private String plot;

    private Long views;

    private String status;

    private String category;

    private Integer subscriptionFee;

    private String plotUrl;

    private Boolean isBest;

    private String title;

    private String authorName;

    @PostRemove
    public void onPostRemove() {
        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }

    public static BookRepository repository() {
        BookRepository bookRepository = LibraryApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    //<<< Clean Arch / Port Method
    public void readBook(ReadBookCommand readBookCommand) {
        //implement business logic here:

        BookRead bookRead = new BookRead(this);
        bookRead.publishAfterCommit();
        BookReadFailed bookReadFailed = new BookReadFailed(this);
        bookReadFailed.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void bookPublish(BookPublished bookPublished) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BookPublished bookPublished = new BookPublished(book);
        bookPublished.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookPublished.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BookPublished bookPublished = new BookPublished(book);
            bookPublished.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void archiveBestseller(BookRead bookRead) {
        //implement business logic here:

        /** Example 1:  new item 
        Book book = new Book();
        repository().save(book);

        BestsellerArchived bestsellerArchived = new BestsellerArchived(book);
        bestsellerArchived.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookRead.get???()).ifPresent(book->{
            
            book // do something
            repository().save(book);

            BestsellerArchived bestsellerArchived = new BestsellerArchived(book);
            bestsellerArchived.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
