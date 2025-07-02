package aivlelibraryminiproj.domain;

import aivlelibraryminiproj.ServiceaiApplication;
import aivlelibraryminiproj.ai.ApplicationContextProvider;
import aivlelibraryminiproj.ai.OpenAiService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

import java.util.Map;

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

    @Lob
    @Column
    private String contents;

    private String coverImageUrl;

    @Lob
    @Column
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

    private static OpenAiService aiService() {
        return ApplicationContextProvider.getBean(OpenAiService.class);
    }

    public void publishBook(PublishBookCommand publishBookCommand) {
        BookPublished bookPublished = new BookPublished(this);
        bookPublished.publishAfterCommit();
    }

    public void regenerateI(RegenerateICommand regenerateICommand) {
        RegenerationRequested regenerationRequested = new RegenerationRequested(this);
        regenerationRequested.publishAfterCommit();
    }

    public static void publicationRegistration(
        BookPublishRequested bookPublishRequested
    ) {
        Publication publication = new Publication();
        publication.setAuthorId(bookPublishRequested.getAuthorId());
        publication.setAuthorname(bookPublishRequested.getAuthorname());
        publication.setScriptId(bookPublishRequested.getId());
        publication.setTitle(bookPublishRequested.getTitle());
        publication.setContents(bookPublishRequested.getContents());
        publication.setStatus("이미지/줄거리 생성 전");
        repository().save(publication);

        BookCoverImagePlotRequest bookCoverImagePlotRequest = new BookCoverImagePlotRequest(publication);
        bookCoverImagePlotRequest.publishAfterCommit();
    }

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
                byte[] coverImageBytes = aiService().generateCoverImage(publication.getTitle(), publication.getAuthorname(), plot, category);

                // 3. 줄거리 pdf와 이미지 파일로 저장
                String baseDir = "./files/";
                String plotFileName = baseDir + "plotFile/" + publication.getId() + "_" + publication.getTitle() + "_plot.pdf";
                String coverFileName = baseDir + "coverImage/" + publication.getId() + "_" + publication.getTitle() + "_cover.jpg";
                String plotUrl = aiService().saveTextAsPdf(publication.getTitle(), publication.getAuthorname(), 
                                                            plot, publication.getContents(), plotFileName);
                String coverImageUrl = aiService().saveBytesToFile(coverImageBytes, coverFileName);
                
                publication.setPlot(plot);
                publication.setPlotUrl(plotUrl);
                publication.setCoverImageUrl(coverImageUrl);
                publication.setCategory(category);
                publication.setSubscriptionFee(1000);
                publication.setStatus("이미지/줄거리 생성완료");

                repository().save(publication);

            }catch(Exception e){
                e.printStackTrace();
            }
            
        });

    }
    
    public static void RegenerateCompleted(
        RegenerationRequested regenerationRequested
    ) {

        repository().findById(regenerationRequested.getId()).ifPresent(publication->{
            
            try{
                byte[] coverImageBytes = aiService().generateCoverImage(publication.getTitle(), publication.getAuthorname(), 
                                                                        publication.getPlot(), publication.getCategory());

                String coverImageUrl = publication.getCoverImageUrl();
                coverImageUrl = aiService().saveBytesToFile(coverImageBytes, coverImageUrl);
                
                publication.setCoverImageUrl(coverImageUrl);
                publication.setStatus("이미지 재생성완료");

                repository().save(publication);

            }catch(Exception e){
                e.printStackTrace();
            }
            
        });

    }

}
