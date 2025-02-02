package org.example.onlineaudiobook.configuration;//package org.example.onlineaudiobook.configuration;
//
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//
//@OpenAPIDefinition(
//        info = @Info(
//                contact = @Contact(
//                        name = "Diyorbek",
//                        email = "sotivoldievdiyorbek@gmail.com"
//                ),
//                description = "OpenApi documentation for Hgp project",
//                title = "OpenApi specification",
//                version = "1.0",
//                license = @License(
//                        name = "License name",
//                        url = "https://some-url.com"
//                ),
//                termsOfService = "Terms of service"
//        ),
//        servers = @Server(
//                description = "Local En",
//                url = "http://localhost:8081"
//        )
//
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        description = "JWT auth description",
//        scheme = "bearer",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        in = SecuritySchemeIn.HEADER
//)
//public class OpenApi {
//
//}
//





import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${server.url}")
    String serverUrl;
    String schemeName = "bearerAuth";
    String bearerFormat = "JWT";
    String scheme = "bearer";

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring 6 Swagger 2 Annotation Example")
                        .description("Spring 6 Swagger Simple Application")
                        .version("11.1.1")
                        .contact(new Contact()
                                .name("Asilbek O'ktamov")
                                .email("oktamovasilbek12@gmail.com"
                                )
                                .url(serverUrl))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs")
                )
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description("Production"))
                )
                .addSecurityItem(
                        new SecurityRequirement().addList(schemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        schemeName, new SecurityScheme()
                                                .name(schemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .bearerFormat(bearerFormat)
                                                .in(SecurityScheme.In.HEADER)
                                                .scheme(scheme))
                );
    }
}