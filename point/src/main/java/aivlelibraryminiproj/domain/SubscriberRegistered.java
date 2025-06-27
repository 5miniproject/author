package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import aivlelibraryminiproj.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class SubscriberRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private Boolean isPurchased;
    private Date registerDate;
    private Date purchaseDate;
    private String notification;
    private Boolean isKt;
}
