package aivlelibraryminiproj.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

//<<< EDA / CQRS
@Entity
@Table(name = "subscription_permissions")
@Data
@NoArgsConstructor
public class SubscriptionPermission {

    // 1. 자동 생성 ID
    // @Id
    // @GeneratedValue(strategy=GenerationType.AUTO)
    // private Long id;

    // @Column(nullable = false)
    // private Long subscriberId;

    // @Column(nullable = false)
    // private Long bookId;

    // public CheckSubsciptionPermisson(Long subscriberId, Long bookId) {
    //     this.subscriberId = subscriberId;
    //     this.bookId = bookId;
    // }

    // 2. 복합 키 사용
    @EmbeddedId
    private SubscriptionPermissionId id;

    public SubscriptionPermission(SubscriptionPermissionId id) {
        this.id = id;
    }
}
