package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class BookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Book>> {

    @Override
    public EntityModel<Book> process(EntityModel<Book> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/readbook")
                .withRel("readbook")
        );

        return model;
    }
}
