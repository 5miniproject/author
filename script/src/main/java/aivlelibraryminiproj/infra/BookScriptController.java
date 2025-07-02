package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/bookScripts")
@RequiredArgsConstructor
@Transactional
public class BookScriptController {

    private final BookScriptRepository bookScriptRepository;

    /**
     * 1) authorId 기준 BookScript 목록 조회
     * GET /bookScripts/author/{authorId}
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookScript>> getBookScriptsByAuthorId(@PathVariable Long authorId) {
        List<BookScript> scripts = bookScriptRepository.findByAuthorId(authorId);
        return ResponseEntity.ok(scripts);
    }

    /**
     * 2) 출간 요청 처리
     * PUT /bookScripts/{id}/scriptpublishrequest
     */
    @PutMapping("/{id}/scriptpublishrequest")
    public ResponseEntity<BookScript> scriptPublishRequest(@PathVariable Long id) throws Exception {
        BookScript bookScript = bookScriptRepository.findById(id)
                .orElseThrow(() -> new Exception("BookScript entity not found with id: " + id));

        // 출간 요청 Command 생성 및 채움
        ScriptPublishRequestCommand command = new ScriptPublishRequestCommand();
        command.setId(bookScript.getId());
        command.setAuthorId(bookScript.getAuthorId());
        command.setTitle(bookScript.getTitle());
        command.setContents(bookScript.getContents());
        command.setAuthorname(bookScript.getAuthorname());
        command.setStatus(bookScript.getStatus());

        // 출간 요청 처리 (도메인 이벤트 발행)
        bookScript.scriptPublishRequest(command);

        // 상태 변경 및 저장
        bookScript.setStatus("REQUESTED");
        bookScriptRepository.save(bookScript);

        return ResponseEntity.ok(bookScript);
    }
}
