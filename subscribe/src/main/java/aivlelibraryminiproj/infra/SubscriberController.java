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
// @RequestMapping(value="/subscribers")
@Transactional
public class SubscriberController {

    @Autowired
    SubscriberRepository subscriberRepository;

    @RequestMapping(
        value = "/subscribers/{id}/purchasesubscription",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Subscriber purchaseSubscription(
        @PathVariable(value = "id") Long id,
        @RequestBody PurchaseSubscriptionCommand purchaseSubscriptionCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /subscriber/purchaseSubscription  called #####"
        );
        Optional<Subscriber> optionalSubscriber = subscriberRepository.findById(
            id
        );

        optionalSubscriber.orElseThrow(() -> new Exception("No Entity Found"));
        Subscriber subscriber = optionalSubscriber.get();
        subscriber.purchaseSubscription(purchaseSubscriptionCommand);

        subscriberRepository.save(subscriber);
        return subscriber;
    }
}
//>>> Clean Arch / Inbound Adaptor
