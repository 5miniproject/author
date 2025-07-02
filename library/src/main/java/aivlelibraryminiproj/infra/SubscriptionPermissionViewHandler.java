package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SubscriptionPermission 리드 모델을 관리하는 전용 핸들러 (뷰 핸들러)
 * 조회용 권한 데이터만 다루기 위해 PolicyHandler에서 분리
 */
@Service
@Transactional
public class SubscriptionPermissionViewHandler {

    private final SubscriptionPermissionRepository permissionRepository;
    public SubscriptionPermissionViewHandler(SubscriptionPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionApplied'"
    )
    public void wheneverBookSubscriptionApplied_CreatePermission(
        @Payload BookSubscriptionApplied event
    ) {
        System.out.println(
            "\n\n##### listener CreatePermission : " + event + "\n\n"
        );

        if (event.getBookId() != null) {
            SubscriptionPermission subscriptionPermission = new SubscriptionPermission();

            subscriptionPermission.setId(event.getId());
            subscriptionPermission.setBookId(event.getBookId());
            subscriptionPermission.setSubscriberId(event.getSubscriberId());

            permissionRepository.save(subscriptionPermission);
        }

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionCancelled'"
    )
    public void wheneverBookSubscriptionCancelled_DeletePermission(
        @Payload BookSubscriptionCancelled event 
    ) {
        System.out.println(
            "\n\n##### listener DeletePermission : " + event + "\n\n"
        );

        if (event.getBookId() != null) {
            SubscriptionPermission subscriptionPermission = new SubscriptionPermission();

            subscriptionPermission.setId(event.getId());
            subscriptionPermission.setBookId(event.getBookId());
            subscriptionPermission.setSubscriberId(event.getSubscriberId());

            permissionRepository.save(subscriptionPermission);
        }
        
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscriptionFailed'"
    )
    public void wheneverBookSubscriptionFailed_DeletePermission(
        @Payload BookSubscriptionFailed event 
    ) {
        System.out.println(
            "\n\n##### listener DeletePermission : " + event + "\n\n"
        );

        if (event.getBookId() != null) {
            SubscriptionPermission subscriptionPermission = new SubscriptionPermission();

            subscriptionPermission.setId(event.getId());
            subscriptionPermission.setBookId(event.getBookId());
            subscriptionPermission.setSubscriberId(event.getSubscriberId());

            permissionRepository.save(subscriptionPermission);
        }
        
    }

}