package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.List;
// import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "subscriptionPermissions",
    path = "subscriptionPermissions"
)
public interface SubscriptionPermissionRepository
    extends JpaRepository<SubscriptionPermission, SubscriptionPermissionId> {
        // 1. 자동 생성 ID
        // boolean existsBySubscriberIdAndBookId(Long subscriberId, Long bookId);
        // Optional<CheckSubscriptionPermisson> findBySubscriberIdAndBookId(Long subscriberId, Long bookId);

        // 2. 복합 키
        // 복합 키 자체가 존재 여부를 보장하기 때문에 boolean 메서드 필요 X
        void deleteById(SubscriptionPermissionId id);
    }
