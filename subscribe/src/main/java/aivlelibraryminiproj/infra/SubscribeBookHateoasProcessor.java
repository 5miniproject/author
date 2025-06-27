package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class SubscribeBookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<SubscribeBook>> {

    @Override
    public EntityModel<SubscribeBook> process(
        EntityModel<SubscribeBook> model
    ) {
        return model;
    }
}
