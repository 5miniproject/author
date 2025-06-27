package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "subscriptionOpens",
    path = "subscriptionOpens"
)
public interface SubscriptionOpenRepository
    extends PagingAndSortingRepository<SubscriptionOpen, Long> {}
