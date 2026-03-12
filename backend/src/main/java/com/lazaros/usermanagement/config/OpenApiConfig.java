package com.lazaros.usermanagement.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userManagementOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("User Management API")
                .description("Technical assessment API for managing users and system profiles")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Pedro Henrique")))
            .externalDocs(new ExternalDocumentation()
                .description("Project repository documentation"));
    }
}