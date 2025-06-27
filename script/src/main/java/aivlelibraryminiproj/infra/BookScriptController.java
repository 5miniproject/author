package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        @PathVariable(value = "id") Long id,
        @RequestBody ScriptPublishRequestCommand scriptPublishRequestCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /bookScript/scriptPublishRequest  called #####"
        );
        Optional<BookScript> optionalBookScript = bookScriptRepository.findById(
            id
        );

        optionalBookScript.orElseThrow(() -> new Exception("No Entity Found"));
        BookScript bookScript = optionalBookScript.get();
        bookScript.scriptPublishRequest(scriptPublishRequestCommand);

        bookScriptRepository.save(bookScript);
        return bookScript;
    }
}
//>>> Clean Arch / Inbound Adaptor
