package com.example.upstdc.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("UPSTDC Infrastructure Monitoring API")
                .description("REST API for Project, Tenders, Progress, Inspections, Payments, Documents and Reporting with JWT Security.")
                .version("1.0.0")
                .contact(new Contact().name("UPSTDC").email("support@upstdc.local"))
                .license(new License().name("Proprietary")))
            .addTagsItem(new Tag().name("Auth").description("Authentication and token refresh"))
            .addTagsItem(new Tag().name("Users").description("User and roles management"))
            .addTagsItem(new Tag().name("Projects").description("Project CRUD and lifecycle"))
            .addTagsItem(new Tag().name("Tenders").description("Tender and contractor management"))
            .addTagsItem(new Tag().name("Progress").description("Geo-tagged project progress"))
            .addTagsItem(new Tag().name("Inspections").description("Inspection and handover workflows"))
            .addTagsItem(new Tag().name("Payments").description("Fund allocation and payments"))
            .addTagsItem(new Tag().name("Documents").description("File upload & download"))
            .addTagsItem(new Tag().name("Reports").description("Reporting and summaries"))
            .externalDocs(new ExternalDocumentation().description("Swagger UI").url("/swagger-ui.html"));
    }
}
