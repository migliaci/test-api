package com.migliaci.myretail.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Responsible for initializing Swagger docket.
 *
 * @Author migliaci
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    private static ApiInfo apiInfo() {
        Contact contact = new Contact("Michael Migliacio", null, "migliaci@gmail.com");

        return new ApiInfo("MyRetail API",
                "Basic retail API implementation using Java, Spring, Gradle, and Groovy.",
                null,
                null,
                contact,
                null,
                null);
    }
}
