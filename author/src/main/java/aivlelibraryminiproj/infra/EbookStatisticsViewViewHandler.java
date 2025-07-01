package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.config.kafka.KafkaProcessor;
import aivlelibraryminiproj.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EbookStatisticsViewViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private EbookStatisticsViewRepository ebookStatisticsViewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookRegistered_then_CREATE_1(
        @Payload BookRegistered bookRegistered
    ) {
        try {
            if (!bookRegistered.validate()) return;

            // view 객체 생성
            EbookStatisticsView ebookStatisticsView = new EbookStatisticsView();
            // view 객체에 이벤트의 Value 를 set 함
            ebookStatisticsView.setSubscriptionCount(
                bookRegistered.getSubscriptionCount()
            );
            ebookStatisticsView.setAuthorId(bookRegistered.getAuthorId());
            ebookStatisticsView.setBookId(bookRegistered.getId());
            ebookStatisticsView.setIsBestSeller(
                bookRegistered.getIsBestSeller()
            );
            ebookStatisticsView.setCoverImageUrl(
                bookRegistered.getCoverImageUrl()
            );
            // view 레파지 토리에 save
            ebookStatisticsViewRepository.save(ebookStatisticsView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionApplied_then_UPDATE_1(
        @Payload BookSubscriptionApplied bookSubscriptionApplied
    ) {
        try {
            if (!bookSubscriptionApplied.validate()) return;
            // view 객체 조회

            List<EbookStatisticsView> ebookStatisticsViewList = ebookStatisticsViewRepository.findByBookId(
                bookSubscriptionApplied.getBookId()
            );
            for (EbookStatisticsView ebookStatisticsView : ebookStatisticsViewList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                ebookStatisticsView.setSubscriptionCount(
                    ebookStatisticsView.getSubscriptionCount() + 1
                );
                // view 레파지 토리에 save
                ebookStatisticsViewRepository.save(ebookStatisticsView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBestsellerArchived_then_UPDATE_2(
        @Payload BestsellerArchived bestsellerArchived
    ) {
        try {
            if (!bestsellerArchived.validate()) return;
            // view 객체 조회

            List<EbookStatisticsView> ebookStatisticsViewList = ebookStatisticsViewRepository.findByBookId(
                bestsellerArchived.getId()
            );
            for (EbookStatisticsView ebookStatisticsView : ebookStatisticsViewList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                ebookStatisticsView.setIsBestSeller(true);
                // view 레파지 토리에 save
                ebookStatisticsViewRepository.save(ebookStatisticsView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionCancelled_then_UPDATE_3(
        @Payload BookSubscriptionCancelled bookSubscriptionCancelled
    ) {
        try {
            if (!bookSubscriptionCancelled.validate()) return;
            // view 객체 조회

            List<EbookStatisticsView> ebookStatisticsViewList = ebookStatisticsViewRepository.findByBookId(
                bookSubscriptionCancelled.getBookId()
            );
            for (EbookStatisticsView ebookStatisticsView : ebookStatisticsViewList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                ebookStatisticsView.setSubscriptionCount(
                    ebookStatisticsView.getSubscriptionCount() - 1
                );
                // view 레파지 토리에 save
                ebookStatisticsViewRepository.save(ebookStatisticsView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @StreamListener(KafkaProcessor.INPUT)
    public void whenBookSubscriptionCancelled_then_UPDATE_4(
        @Payload BookSubscriptionFailed BookSubscriptionFailed
    ) {
        try {
            if (!BookSubscriptionFailed.validate()) return;
            // view 객체 조회

            List<EbookStatisticsView> ebookStatisticsViewList = ebookStatisticsViewRepository.findByBookId(
                BookSubscriptionFailed.getBookId()
            );
            for (EbookStatisticsView ebookStatisticsView : ebookStatisticsViewList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                ebookStatisticsView.setSubscriptionCount(
                    ebookStatisticsView.getSubscriptionCount() - 1
                );
                // view 레파지 토리에 save
                ebookStatisticsViewRepository.save(ebookStatisticsView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBestSellerCancelled_then_UPDATE_5(
        @Payload BestSellerCancelled bestSellerCancelled
    ) {
        try {
            if (!bestSellerCancelled.validate()) return;
            // view 객체 조회

            List<EbookStatisticsView> ebookStatisticsViewList = ebookStatisticsViewRepository.findByBookId(
                bestSellerCancelled.getId()
            );
            for (EbookStatisticsView ebookStatisticsView : ebookStatisticsViewList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                ebookStatisticsView.setIsBestSeller(false);
                // view 레파지 토리에 save
                ebookStatisticsViewRepository.save(ebookStatisticsView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookDeleted_then_DELETE_1(
        @Payload BookDeleted bookDeleted
    ) {
        try {
            if (!bookDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            ebookStatisticsViewRepository.deleteByBookId(bookDeleted.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
