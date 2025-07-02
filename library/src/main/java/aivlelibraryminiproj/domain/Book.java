package aivlelibraryminiproj.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import aivlelibraryminiproj.LibraryApplication;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@Where(clause = "status != 'DELETED'") // soft delete의 경우 findAll하면 다 찾아지는 것 방지
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @Column(nullable = false, unique = true)
    private Long publicationId;

    // @OneToOne
    // Publication publication;

    private Long authorId;    
    private String authorName;
    private String title;

    @Lob
    @Column
    private String contents;

    @Lob
    @Column
    private String plot; // 요약

    private String plotUrl; // 요약 위치
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
    private Boolean isBestSeller = false;
    private Long views = 0L;
    private Long subscriptionCount = 0L;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public enum BookStatus {
        REGISTERED,      
        DELETED
    }

    @PostPersist
    public void onPostPersist() {
        BookRegistered bookRegistered = new BookRegistered(this);
        bookRegistered.publishAfterCommit();
    }

    public static BookRepository repository() {
        BookRepository bookRepository = LibraryApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    public static void publishBook(
        BookPublished bookPublished
    ) {
        Book book = new Book();
        book.setPublicationId(bookPublished.getId());
        book.setAuthorId(bookPublished.getAuthorId());
        book.setAuthorName(bookPublished.getAuthorname());
        book.setTitle(bookPublished.getTitle());
        book.setContents(bookPublished.getContents());
        book.setPlot(bookPublished.getPlot());
        book.setPlotUrl(bookPublished.getPlotUrl());
        book.setCoverImageUrl(bookPublished.getCoverImageUrl());
        book.setCategory(bookPublished.getCategory());
        book.setSubscriptionFee(bookPublished.getSubscriptionFee());
        book.setStatus(BookStatus.REGISTERED);

        repository().save(book);
    }

    public static void archiveBestseller(
        BookSubscriptionApplied bookSubscriptionApplied
    ) {
        repository().findById(bookSubscriptionApplied.getBookId()).ifPresent(book -> {
            book.setSubscriptionCount(book.getSubscriptionCount() + 1);

            if(book.getSubscriptionCount() >= 5 && !book.getIsBestSeller()){
                book.setIsBestSeller(true);
            }

            BestSellerArchived bestSellerArchived = new BestSellerArchived(book);
            bestSellerArchived.publishAfterCommit();
        });
    }

    public static void cancelBestSeller(
        Long id
    ) {
        repository().findById(id).ifPresent(book -> {
            book.setSubscriptionCount(book.getSubscriptionCount() - 1);

            if(book.getSubscriptionCount() < 5 && book.getIsBestSeller()){
                book.setIsBestSeller(false);
            }

            BestSellerCancelled bestSellerCancelled = new BestSellerCancelled(book);
            bestSellerCancelled.publishAfterCommit();
        });
    }
    
    // 비즈니스 로직 4. 책 열람
    //<<< Clean Arch / Port Method
    public void readBook(ReadBookCommand readBookCommand) {
        this.views++;

        BookRead bookRead = new BookRead(this);

        if (readBookCommand != null && readBookCommand.getSubscriberId() != null) {
            bookRead.setSubscriberId(readBookCommand.getSubscriberId());
        }

        bookRead.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    // 비즈니스 로직 5. 책 삭제
    // hard delete
    @PreRemove
    public void onPreRemove() {
        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }
    /* soft delete
    public void deleteBook() {
        this.status = BookStatus.DELETED;

        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }
    */

}
//>>> DDD / Aggregate Root
