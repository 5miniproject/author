package aivlelibraryminiproj.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import aivlelibraryminiproj.LibraryApplication;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
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
    private Long subscriptionCount = 0L;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public enum BookStatus {
        REGISTERED,      
        DELETED
    }
    // private Boolean isDeleted;

    // 비즈니스 로직 1. 도서 등록 (생성자 사용)
    public Book(BookPublished bookPublished) {
        this.publicationId = bookPublished.getPublicationId();
        this.authorId = bookPublished.getAuthorId();
        this.authorName = bookPublished.getAuthorName();
        this.title = bookPublished.getTitle();
        this.contents = bookPublished.getContents();
        this.plot = bookPublished.getPlot();
        this.plotUrl = bookPublished.getPlotUrl();
        this.coverImageUrl = bookPublished.getCoverImageUrl();
        this.category = bookPublished.getCategory();
        this.subscriptionFee = bookPublished.getSubscriptionFee();
        this.status = BookStatus.REGISTERED;
    }

    @PostPersist
    public void onPostPersist() {
        BookRegistered bookRegistered = new BookRegistered(this);
        bookRegistered.publishAfterCommit();
    }

    // 비즈니스 로직 2. 구독 수 증가 및 베스트셀러 선정
    public void increaseCountAndCheckBestseller() {
        this.subscriptionCount++;

        if (this.subscriptionCount >= 5 && !this.isBestSeller) {
            this.isBestSeller = true;

            BestSellerArchived bestSellerArchived = new BestSellerArchived(this);
            bestSellerArchived.publishAfterCommit();
        }
    }

    // 비즈니스 로직 3. 구독 취소 및 베스트셀러 취소
    public void decreaseCountAndCheckBestseller() {
        // 1. 구독 횟수가 0보다 클 때만 감소
        if (this.subscriptionCount > 0) {
            this.subscriptionCount--;
        }

        // 2. 구독 횟수가 5회 미만이 되고, 현재 베스트셀러 상태일 경우
        if (this.subscriptionCount < 5 && this.isBestSeller) {        
            this.isBestSeller = false;

            BestSellerCancelled bestSellerCancelled = new BestSellerCancelled(this);
            bestSellerCancelled.publishAfterCommit();
        }
    }
    
    //<<< Clean Arch / Port Method
    public void readBook(ReadBookCommand readBookCommand) {
        

        BookRead bookRead = new BookRead(this);
        bookRead.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    
    // @PostUpdate
    // public void onPostUpdate() {
    //     BookRead bookRead = new BookRead();
    //     bookRead.publishAfterCommit();
    // }

    //<<< Clean Arch / Port Method
    public void deleteBook(DeleteBookCommand deleteBookCommand) {
        

        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    
    @PreRemove
    public void onPreRemove() {
        BookDeleted bookDeleted = new BookDeleted(this);
        bookDeleted.publishAfterCommit();
    }

    // public static BookRepository repository() {
    //     BookRepository bookRepository = LibraryApplication.applicationContext.getBean(
    //         BookRepository.class
    //     );
    //     return bookRepository;
    // }
}
//>>> DDD / Aggregate Root
