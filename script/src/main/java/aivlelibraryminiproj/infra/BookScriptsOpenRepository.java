package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "bookScriptsOpens",
    path = "bookScriptsOpens"
)
public interface BookScriptsOpenRepository
    extends PagingAndSortingRepository<BookScriptsOpen, Long> {}
