package aivlelibraryminiproj.domain;
import aivlelibraryminiproj.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BestSellerCancelled extends AbstractEvent{
    
    private Long id;
    private Boolean isBestSeller;

}
