package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PurchaseSubscriptionCommand {

    private Boolean isPurchased;
    private Date purchaseDate;
}
