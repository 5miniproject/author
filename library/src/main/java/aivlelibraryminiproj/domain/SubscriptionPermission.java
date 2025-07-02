package aivlelibraryminiproj.domain;

// import java.time.LocalDate;
// import java.util.Date;
// import java.util.List;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

//<<< EDA / CQRS
@Entity
@Table(name = "subscription_permissions")
@Data
@NoArgsConstructor
public class SubscriptionPermission {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long subscriberId;

    @Column(nullable = false)
    private Long bookId;

}
