// EbookStatisticsViewViewHandler.java
package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EbookStatisticsViewViewHandler {

    @Autowired
    private EbookStatisticsViewRepository ebookStatisticsViewRepository;

    // 작가등록됨 → ReadModel 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorRegistered_then_CREATE(@Payload AuthorRegistered event) {
        if (!event.validate()) return;

        EbookStatisticsView view = new EbookStatisticsView();
        view.setId(event.getId());
        view.setPublicationId(event.getEmail()); // 실제 publicationId 매핑 필요
        view.setViews(0L); // 신규 등록 시 0으로 초기화
        view.setIsApprove(event.getIsApprove()); // 등록 시 승인상태도 반영
        ebookStatisticsViewRepository.save(view);
    }

    // 작가승인됨 → 승인 상태 갱신
    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorApproved_then_UPDATE(@Payload AuthorApproved event) {
        if (!event.validate()) return;

        Optional<EbookStatisticsView> optionalView = ebookStatisticsViewRepository.findById(event.getId());
        if (optionalView.isPresent()) {
            EbookStatisticsView view = optionalView.get();
            view.setIsApprove(event.getIsApprove()); // 실제 승인 상태 반영
            ebookStatisticsViewRepository.save(view);
        }
    }

    // 작가거절됨 → 승인 상태 갱신
    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorRejected_then_UPDATE(@Payload AuthorRejected event) {
        if (!event.validate()) return;

        Optional<EbookStatisticsView> optionalView = ebookStatisticsViewRepository.findById(event.getId());
        if (optionalView.isPresent()) {
            EbookStatisticsView view = optionalView.get();
            view.setIsApprove(event.getIsApprove()); // 실제 승인 상태 반영
            ebookStatisticsViewRepository.save(view);
        }
    }

    // 작가삭제됨 → ReadModel 삭제
    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorDeleted_then_DELETE(@Payload AuthorDeleted event) {
        if (!event.validate()) return;
        ebookStatisticsViewRepository.deleteById(event.getId());
    }
}
