package aivlelibraryminiproj.infra;

import aivlelibraryminiproj.domain.BookScript;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

@Configuration
public class RestRepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.getExposureConfiguration()
            .forDomainType(BookScript.class)
            .withItemExposure((metadata, httpMethods) -> httpMethods.disable(HttpMethod.PUT))
            .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(HttpMethod.PUT));
    }
}
