package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BestSellerArchived extends AbstractEvent {

    private Long bookId;
    private Boolean isBestSeller;

}
