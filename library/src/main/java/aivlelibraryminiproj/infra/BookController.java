package aivlelibraryminiproj.infra;

// import aivlelibraryminiproj.infra.*;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;

import aivlelibraryminiproj.domain.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

// 정렬
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

//<<< Clean Arch / Inbound Adaptor
@RestController
@RequestMapping(value="/books")
@Transactional
public class BookController {

    // 1. 필드 주입
    // @Autowired
    // BookRepository bookRepository;

    // 2. 생성자 주입 -> 강력 권장
    private final BookRepository bookRepository;
    private final SubscriptionPermissionRepository permissionRepository;

    // @Autowired
    public BookController(BookRepository bookRepository,
    SubscriptionPermissionRepository permissionRepository) {
        this.bookRepository = bookRepository;
        this.permissionRepository = permissionRepository;
    }

    // @RequestMapping(
    //     value = "/{id}/read",
    //     method = RequestMethod.GET,
    //     produces = "application/json;charset=UTF-8"
    // )
    /* 
    GET: readBook은 views를 증가시키고 BookRead 이벤트를 발행하므로 GET은 부적합
    PUT: 멱등성(같은 요청을 보내도 서버의 상태가 유지되어야함)을 보장해야 하는데 readBook은 views를 계속 변화시키니 멱등성 보장 X
    -> 따라서 POST가 맞다
    */
    // @PutMapping(value = "/{id}/read") 
    @PostMapping(value = "/{id}/read") 
    public Book readBook(
        @PathVariable(value = "id") Long bookId,
        @RequestBody ReadBookCommand readBookCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /book/read called #####");

        SubscriptionPermissionId permissionId = new SubscriptionPermissionId(
            readBookCommand.getSubscriberId(),
            bookId
        );

        boolean hasPermission = permissionRepository.existsById(permissionId);

        if (!hasPermission) {
            // 권한이 없으면 403 Forbidden 에러를 반환
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 책을 열람할 구독 권한이 없습니다.");
        }

        // 1. Repository를 통해 애그리거트 조회
        Book book = bookRepository.findById(bookId).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 책을 찾을 수 없습니다."));

        // soft delete 구현 시
        if (book.getStatus() == Book.BookStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 책입니다.");
        }

        // 2. 애그리거트의 비즈니스 메서드 호출
        book.readBook(readBookCommand);

        // 3. 애그리거트 상태 저장
        bookRepository.save(book);

        return book;
    }

    // 삭제는 보통 주체의 정보(ID 등) 외에 추가 데이터가 필요 없으니 DTO 생략 (DeleteBookCommand 필요 X)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Long bookId) throws Exception {
        try {
            System.out.println("##### /book/delete called #####");

            Book book = bookRepository.findById(bookId).orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제하려는 책을 찾을 수 없습니다."));

            // hard delete: 물리적으로 DB에서 삭제하기
            bookRepository.delete(book);

            // soft delete: DB에서 삭제하는 게 아니라 상태만 변경
            // book.deleteBook();
            // bookRepository.save(book);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }

    // 책들을 정렬할 때, 베스트 셀러 우선으로 정렬
    // 정렬 사용 방법
    // 1. 기본 정렬 (베스트셀러 - 조회수 - 최신 등록일): GET /books
    // 2. 조회수 순: GET /books?sortBy=views
    // 3. 최신 등록일 순: GET /books?sortBy=date
    @GetMapping
    public ResponseEntity<Page<BookSummaryDto>> listBooks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "default") String sortBy) {
            Sort sort = createSortByOption(sortBy);

            // 페이지 번호와 사이즈, 정렬 규칙을 담은 Pageable 객체 생성
            Pageable pageable = PageRequest.of(page, size, sort);

            // Repository에 Pageable 객체 전달
            Page<Book> bookPage = bookRepository.findAll(pageable);

            Page<BookSummaryDto> bookSummaryDtoPage = bookPage.map(BookSummaryDto::new);

            // 조회된 결과를 ResponseEntity에 담아 반환
            return ResponseEntity.ok(bookSummaryDtoPage);
        }

    private Sort createSortByOption(String sortBy) {
        switch(sortBy) {
            case "views":
                return Sort.by(Sort.Order.desc("views"));
            case "date":
                return Sort.by(Sort.Order.desc("publishedAt"));
            case "default":
            default:
                // 기본 정렬 규칙
                // 1. isBestSeller true
                // 2. 조회수
                // 3. publishedAt 기준으로 (미구현)
                return Sort.by(
                    Sort.Order.desc("isBestSeller"),
                    Sort.Order.desc("views")
                    // Sort.Order.desc("publishedAt")
                );
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
