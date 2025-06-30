package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "ebookStatisticsViews",
    path = "ebookStatisticsViews"
)
public interface EbookStatisticsViewRepository
    extends PagingAndSortingRepository<EbookStatisticsView, Long> {}


    