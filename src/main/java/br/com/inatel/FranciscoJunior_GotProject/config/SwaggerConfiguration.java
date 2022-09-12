package br.com.inatel.FranciscoJunior_GotProject.config;

import org.aspectj.bridge.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class to configure documentation via swagger
 * @author francisco.carvalho
 * @since 1.0
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String title = "Game of Thrones Project (GoT Project)";
    private static final String description = "The got project consists of manipulating characters and situations from " +
            "the world of Game of Thrones, being able to search, insert and delete objects. We can create a list of " +
            "families and do the death count for each family as well.";
    private static final String version = "1.0";
    private static final String name = "Francisco Junior";
    private static final String url = "https://github.com/JuninhoCarvalho/GoT_Project";
    private static final String email = "francisco.carvalho@inatel.br";

    /**
     * Method where the api information will be provided
     * @return Docket
     */
    @Bean
    public Docket SwaggerConfig() {
        Contact contact = new Contact(name, url, email);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Message.class)
                .apiInfo(new ApiInfoBuilder()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(contact)
                        .build());
    }
}
