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
// @RequestMapping(value="/publications")
@Transactional
public class PublicationController {

    @Autowired
    PublicationRepository publicationRepository;

    @RequestMapping(
        value = "/publications/{id}/publishbook",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Publication publishBook(
        @PathVariable(value = "id") Long id,
        @RequestBody PublishBookCommand publishBookCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /publication/publishBook  called #####");
        Optional<Publication> optionalPublication = publicationRepository.findById(
            id
        );

        optionalPublication.orElseThrow(() -> new Exception("No Entity Found"));
        Publication publication = optionalPublication.get();
        publication.publishBook(publishBookCommand);

        publicationRepository.save(publication);
        return publication;
    }

    @RequestMapping(
        value = "/publications/{id}/regeneratei",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Publication regenerateI(
        @PathVariable(value = "id") Long id,
        @RequestBody RegenerateICommand regenerateICommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /publication/regenerateI  called #####");
        Optional<Publication> optionalPublication = publicationRepository.findById(
            id
        );

        optionalPublication.orElseThrow(() -> new Exception("No Entity Found"));
        Publication publication = optionalPublication.get();
        publication.regenerateI(regenerateICommand);

        publicationRepository.save(publication);
        return publication;
    }
}
//>>> Clean Arch / Inbound Adaptor
