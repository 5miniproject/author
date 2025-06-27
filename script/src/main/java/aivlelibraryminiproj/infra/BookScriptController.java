package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/bookScripts")
@Transactional
public class BookScriptController {

    @Autowired
    BookScriptRepository bookScriptRepository;

    @RequestMapping(
        value = "/bookScripts/{id}/scriptpublishrequest",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public BookScript scriptPublishRequest(
        @PathVariable(value = "id") Long id//,
        // @RequestBody ScriptPublishRequestCommand scriptPublishRequestCommand,
        // HttpServletRequest request,
        // HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /bookScript/scriptPublishRequest  called #####"
        );
        Optional<BookScript> optionalBookScript = bookScriptRepository.findById(
            id
        );

        optionalBookScript.orElseThrow(() -> new Exception("No Entity Found"));
        BookScript bookScript = optionalBookScript.get();

        // ✅ ScriptPublishRequestCommand 생성 및 채우기
        ScriptPublishRequestCommand command = new ScriptPublishRequestCommand();
        command.setId(bookScript.getId());
        command.setAuthorId(bookScript.getAuthorId());
        command.setTitle(bookScript.getTitle());
        command.setContents(bookScript.getContents());
        command.setAuthorname(bookScript.getAuthorname());
        command.setStatus(bookScript.getStatus());

        bookScript.scriptPublishRequest(command);

        bookScript.setStatus("REQUESTED");
        
        bookScriptRepository.save(bookScript);
        return bookScript;
    }
}
//>>> Clean Arch / Inbound Adaptor
