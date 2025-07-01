package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subscribeBooks",
    path = "subscribeBooks"
)
public interface SubscribeBookRepository
    extends PagingAndSortingRepository<SubscribeBook, Long> {
        // ✅ 구독 중복 여부 확인 메서드 추가
        boolean existsBySubscriberIdAndBookId(Long subscriberId, Long bookId);
    }
