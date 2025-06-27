package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "publications",
    path = "publications"
)
public interface PublicationRepository
    extends PagingAndSortingRepository<Publication, Long> {}
