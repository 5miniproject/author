package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class PublicationHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Publication>> {

    @Override
    public EntityModel<Publication> process(EntityModel<Publication> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/publishbook")
                .withRel("publishbook")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/regeneratei")
                .withRel("regeneratei")
        );

        return model;
    }
}
