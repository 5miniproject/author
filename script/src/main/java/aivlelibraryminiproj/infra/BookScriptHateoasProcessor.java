package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class BookScriptHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<BookScript>> {

    @Override
    public EntityModel<BookScript> process(EntityModel<BookScript> model) {
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/scriptpublishrequest"
                )
                .withRel("scriptpublishrequest")
        );

        return model;
    }
}
