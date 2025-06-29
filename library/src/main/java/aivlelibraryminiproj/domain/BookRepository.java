package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    // Optional<BookRead> findByBookReadId(Long id);
    // Pageable 객체를 통해 페이징 및 정렬 규칙을 동적으로 적용할 수 있음
    // PagingAndSortingRepository를 상속하면 기본적으로 이 메서드가 포함되어 있지만, 명시적으로 선언하여 의도를 명확히 할 수 있음
    Page<Book> findAll(Pageable pageable);
}
