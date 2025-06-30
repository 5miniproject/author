package aivlelibraryminiproj.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookSummaryDto {
    private Long id;
    private String authorName;
    private String title;
    private String coverImageUrl;
    private String category;
    private Integer subscriptionFee;
    private Boolean isBestSeller = false;
    private Long views = 0L;
    private Long subscriptionCount = 0L;

    public BookSummaryDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authorName = book.getAuthorName();
        this.coverImageUrl = book.getCoverImageUrl();
        this.category = book.getCategory();
        this.subscriptionFee = book.getSubscriptionFee();
        this.isBestSeller = book.getIsBestSeller();
        this.views = book.getViews();
    }
}
