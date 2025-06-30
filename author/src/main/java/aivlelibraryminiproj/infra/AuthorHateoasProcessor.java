package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class AuthorHateoasProcessor implements RepresentationModelProcessor<EntityModel<Author>> {
    @Override
    public EntityModel<Author> process(EntityModel<Author> model) {
        model.add(
            Link.of(model.getRequiredLink("self").getHref() + "/approveauthor")
                .withRel("approveauthor")
        );
        model.add(
            Link.of(model.getRequiredLink("self").getHref() + "/rejectauthor")
                .withRel("rejectauthor")
        );
        return model;
    }
}
