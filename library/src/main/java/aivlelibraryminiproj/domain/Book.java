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
@Table(name = "books")
@Data
@NoArgs
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long publicationId;

    private Long authorId;    
    private String authorName;
    private String title;

    @Lob
    private String contents;

    @Lob
    private String plot; // 요약

    private String plotUrl; // 요약 위치
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
    private Boolean isBestSeller = false;
    private Long views = 0L;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public enum BookStatus {
        PUBLISHED,      // 출간 완료
        DELETED
    }
    // private Boolean isDeleted;

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
    public static void registerBook(BookPublished bookPublished) {
        Book book = new Book(bookPublished);

        repository().save(book);

        BookRegistered bookRegistered = new BookRegistered(book);
        bookRegistered.publishAfterCommit();

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void archiveBestseller(BookRead bookRead) {
        repository().findById(bookRead.getBookId()).ifPresent(book->{
            
            if (book.getViews() => 5 && !book.getIsBestSeller()) {
                book.setIsBestSeller(true);

                repository().save(book);
            }

            BestsellerArchived bestsellerArchived = new BestsellerArchived(book);
            bestsellerArchived.publishAfterCommit();

         });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
