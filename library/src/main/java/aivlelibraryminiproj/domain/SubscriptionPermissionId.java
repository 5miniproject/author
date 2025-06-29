package aivlelibraryminiproj.domain;

import lombok.*;
import javax.persistence.Embeddable;
import java.io.Serializable;

// subscriberId, bookId를 이용한 복합 키
@Embeddable
@Data // @Getter, @Setter, @EqualsAndHashCode, @ToString 등을 포함
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPermissionId implements Serializable {
    private Long subscriberId;
    private Long bookId;
}