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
// @RequestMapping(value="/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
        value = "/books/{id}/readbook",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Book readBook(
        @PathVariable(value = "id") Long id,
        @RequestBody ReadBookCommand readBookCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /book/readBook  called #####");
        Optional<Book> optionalBook = bookRepository.findById(id);

        optionalBook.orElseThrow(() -> new Exception("No Entity Found"));
        Book book = optionalBook.get();
        book.readBook(readBookCommand);

        bookRepository.save(book);
        return book;
    }
}
//>>> Clean Arch / Inbound Adaptor
