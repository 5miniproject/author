package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.PointApplication;
import aivlelibraryminiproj.domain.PointAdded;
import aivlelibraryminiproj.domain.PointDecreased;
import aivlelibraryminiproj.domain.PointShorted;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private Integer point;

    private Boolean isKt;

    private Boolean isPurchased;

    public static PointRepository repository() {
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void pointAdd(SubscriberRegistered subscriberRegistered) {

        Point point = new Point();
        point.setUserId(subscriberRegistered.getId());
        point.setIsKt(subscriberRegistered.getIsKt());
        point.setPoint(subscriberRegistered.getIsKt() ? 5000 : 1000);
        point.setIsPurchased(subscriberRegistered.getIsPurchased());
        repository().save(point);

        PointAdded pointAdded = new PointAdded(point);
        pointAdded.publishAfterCommit();

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void pointUse(
        BookSubscriptionApplied bookSubscriptionApplied
    ) {

        repository().findById(bookSubscriptionApplied.getSubscriberId()).ifPresent(point->{
            
            Integer usePoint = point.getIsPurchased() ? 0 : bookSubscriptionApplied.getSubscriptionFee();
            
            if(point.getPoint() >= usePoint){
                point.setPoint(point.getPoint() - usePoint);

                PointDecreased pointDecreased = new PointDecreased(point);
                pointDecreased.publishAfterCommit();
                repository().save(point);
            }else{
                PointShorted pointShorted = new PointShorted(point);
                pointShorted.publishAfterCommit();
            }

         });

    }
    //>>> Clean Arch / Port Method

    public static void subscriptionPurchased(
        SubscriptionPurchased subscriptionPurchased
    ) {

        repository().findById(subscriptionPurchased.getId()).ifPresent(point->{
            point.setIsPurchased(true);
            repository().save(point);
         });

    }

}
//>>> DDD / Aggregate Root
