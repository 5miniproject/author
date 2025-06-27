package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.ServiceaiApplication;
import aivlelibraryminiproj.ai.ApplicationContextProvider;
import aivlelibraryminiproj.ai.OpenAiService;
import aivlelibraryminiproj.domain.BookCoverImagePlotRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Publication_table")
@Data
//<<< DDD / Aggregate Root
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;

    private Long scriptId;

    private String title;

    private String contents;

    private String coverImageUrl;

    private String plot;

    private String status;

    private String plotUrl;

    private String category;

    private Integer subscriptionFee;

    private String authorname;

    public static PublicationRepository repository() {
        PublicationRepository publicationRepository = ServiceaiApplication.applicationContext.getBean(
            PublicationRepository.class
        );
        return publicationRepository;
    }

    //<<< Clean Arch / Port Method
    public void publishBook(PublishBookCommand publishBookCommand) {
        //implement business logic here:

        BookPublished bookPublished = new BookPublished(this);
        bookPublished.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void regenerateI(RegenerateICommand regenerateICommand) {
        //implement business logic here:

        RegenerationRequested regenerationRequested = new RegenerationRequested(
            this
        );
        regenerationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void publicationRegistration(
        BookPublishRequested bookPublishRequested
    ) {
        Publication publication = new Publication();
        publication.setAuthorId(bookPublishRequested.getAuthorId());
        publication.setAuthorname(bookPublishRequested.getAuthorname());
        publication.setScriptId(bookPublishRequested.getId());
        publication.setTitle(bookPublishRequested.getTitle());
        publication.setContents(bookPublishRequested.getContents());
        publication.setStatus("이미지 생성 전");
        repository().save(publication);

        BookCoverImagePlotRequest bookCoverImagePlotRequest = new BookCoverImagePlotRequest(publication);
        bookCoverImagePlotRequest.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void BookCoverImagePlotCompleted(
        BookCoverImagePlotRequest bookCoverImagePlotRequest
    ) {

        repository().findById(bookCoverImagePlotRequest.getId()).ifPresent(publication->{
            
            try{
                // 1. AI에게 줄거리와 카테고리 요청
                Map<String, String> result = aiService().generatePlotAndCategory(publication.getTitle(), publication.getContents());
                String plot = result.get("plot");
                String category = result.get("category");

                // 2. 표지 이미지 생성
                byte[] coverImageBytes = aiService().generateCoverImage(publication.getTitle(), publication.getAuthorname(), 
                                                                        publication.getContents(), category);

                // 3. 줄거리 pdf와 이미지 파일로 저장
                String baseDir = "/workspace/library_project/files/";
                String plotUrl = aiService().saveTextAsPdf(plot, baseDir + publication.getId() + "_plot.pdf");
                String coverImageUrl = aiService().saveBytesToFile(coverImageBytes, baseDir + publication.getId() + "_cover.jpg");

                publication.setPlotUrl(plotUrl);
                publication.setCoverImageUrl(coverImageUrl);
                publication.setCategory(category);
                publication.setSubscriptionFee(10);
                publication.setStatus("이미지/줄거리 생성완료");

                repository().save(publication);
            }catch(Exception e){
                e.printStackTrace();
            }
            

        });

    }

    private static OpenAiService aiService() {
        return ApplicationContextProvider.getBean(OpenAiService.class);
    }

}
//>>> DDD / Aggregate Root
