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

    public static PointRepository repository() {
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void pointAdd(SubscriberRegistered subscriberRegistered) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        PointAdded pointAdded = new PointAdded(point);
        pointAdded.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(subscriberRegistered.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            PointAdded pointAdded = new PointAdded(point);
            pointAdded.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void pointUse(
        BookSubscriptionApplied bookSubscriptionApplied
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        PointDecreased pointDecreased = new PointDecreased(point);
        pointDecreased.publishAfterCommit();
        PointShorted pointShorted = new PointShorted(point);
        pointShorted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookSubscriptionApplied.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            PointDecreased pointDecreased = new PointDecreased(point);
            pointDecreased.publishAfterCommit();
            PointShorted pointShorted = new PointShorted(point);
            pointShorted.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
