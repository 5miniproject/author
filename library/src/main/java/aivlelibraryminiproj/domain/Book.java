package aivlelibraryminiproj.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import aivlelibraryminiproj.LibraryApplication;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
// @NoArgs
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long publicationId;

    // @OneToOne
    // Publication publication;

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
        REGISTERED,      
        DELETED
    }
    // private Boolean isDeleted;

    public static BookRepository repository() {
        BookRepository bookRepository = LibraryApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

   public static void registerBook(BookPublished bookPublished) {
        Book book = new Book();

        book.setPublicationId(bookPublished.getId());
        book.setAuthorId(bookPublished.getAuthorId());
        book.setAuthorName(bookPublished.getAuthorName());
        book.setTitle(bookPublished.getTitle());
        book.setContents(bookPublished.getContents());
        book.setPlot(bookPublished.getPlot());
        book.setPlotUrl(bookPublished.getPlotUrl());
        book.setCoverImageUrl(bookPublished.getCoverImageUrl());
        book.setCategory(bookPublished.getCategory());
        book.setSubscriptionFee(bookPublished.getSubscriptionFee());
        book.setStatus(BookStatus.valueOf("REGISTERED"));

        repository().save(book);

        BookRegistered bookRegistered = new BookRegistered(book);
        bookRegistered.publishAfterCommit();
   }

   public static void archiveBestSeller(BookRead bookRead) {
        if (bookRead.getViews() < 5) { //  || this.getIsBestSeller() == true
            return;
        }

        repository().findById(bookRead.getId()).ifPresentOrElse(book -> {
            book.setIsBestSeller(true);
            repository().save(book);

            BestSellerArchived bestSellerArchived = new BestSellerArchived(book);
            bestSellerArchived.publishAfterCommit();
        }, () -> {
            throw new RuntimeException("\n\n------ 대상을 찾을 수 없습니다. ------\n\n");
        }
        );
   }

   @PostUpdate
   public void onPostUpdate() {
        BookRead bookRead = new BookRead();
        bookRead.publishAfterCommit();
   }

    @PreRemove
    public void onPreRemove() {
        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }
}
//>>> DDD / Aggregate Root
