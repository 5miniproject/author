package aivlelibraryminiproj.infra;

import java.util.List;
import java.util.Optional;
import aivlelibraryminiproj.domain.*;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "subscriptionPermissions",
    path = "subscriptionPermissions"
)
public interface SubscriptionPermissionRepository
    extends JpaRepository<SubscriptionPermission, Long> {

    Optional<SubscriptionPermission> findByBookIdAndSubscriberId(Long bookId, Long subscriberId);
        // 1. 자동 생성 ID
        // boolean existsBySubscriberIdAndBookId(Long subscriberId, Long bookId);
        // Optional<CheckSubscriptionPermisson> findBySubscriberIdAndBookId(Long subscriberId, Long bookId);

        // 2. 복합 키
        // 복합 키 자체가 존재 여부를 보장하기 때문에 boolean 메서드 필요 X
        // void deleteById(SubscriptionPermissionId id); - 선언할 필요 X
    }
