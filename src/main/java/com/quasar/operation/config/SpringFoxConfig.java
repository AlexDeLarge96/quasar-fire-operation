package com.quasar.operation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

    private static final String BASE_ENDPOINT = "/quasar-fire-operation/v1/**";
    private static final String SWAGGER = "swagger-ui.html";
    private static final String WEBJARS = "/webjars/**";
    private static final String CONTROLLERS_PACKAGE = "com.quasar.operation.controllers";
    private static final String CLASSPATH = "classpath:/";
    private static final String PATH_RESOURCES = "META-INF/resources/";
    private static final String PATH_WEBJARS = "META-INF/resources/webjars/";
    private static final String RESOURCES_CLASSPATH = format("%s%s", CLASSPATH, PATH_RESOURCES);
    private static final String WEBJARS_CLASSPATH = format("%s%s", CLASSPATH, PATH_WEBJARS);

    public static final String API_TITLE = "Quasar Fire Operation REST API";
    public static final String API_DESCRIPTION = "Mercado Libre Quasar Fire Operation API Challenge.";
    public static final String API_VERSION = "1.0";
    public static final String AUTHOR = "Bayron Cárdenas";
    public static final String EMAIL = "ingenialex96@gmail.com";
    public static final String LINKEDIN_URL = "https://co.linkedin.com/in/bayron-alexis-c%C3%A1rdenas-espitia-33b978148";
    public static final String LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0.html";
    public static final String LICENSE_APACHE_2_0 = "License: Apache-2.0";

    @Bean
    public Docket api() {
        List<ResponseMessage> deleteResponses = Arrays.asList(
                new ResponseMessageBuilder().code(200).build(),
                new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
        List<ResponseMessage> getResponses = new LinkedList<>(deleteResponses);
        getResponses.add(new ResponseMessageBuilder().code(404).message("Not Found").build());
        List<ResponseMessage> postResponses = new LinkedList<>(getResponses);
        postResponses.add(new ResponseMessageBuilder().code(400).message("Bad Request").build());
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage(CONTROLLERS_PACKAGE))
                .paths(ant(BASE_ENDPOINT))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(DELETE, deleteResponses)
                .globalResponseMessage(GET, getResponses)
                .globalResponseMessage(POST, postResponses);
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(SWAGGER)
                .addResourceLocations(RESOURCES_CLASSPATH);
        registry
                .addResourceHandler(WEBJARS)
                .addResourceLocations(WEBJARS_CLASSPATH);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .license(LICENSE_APACHE_2_0)
                .licenseUrl(LICENSE_URL)
                .contact(new Contact(AUTHOR, LINKEDIN_URL, EMAIL))
                .build();
    }
}
