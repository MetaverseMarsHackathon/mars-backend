package com.hackathon.marsgame.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("mars-game-api")
                .pathsToMatch("/**")
                .build();
    }
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Mars Game API")
                        .description("화성 탐사 게임 백엔드 API 문서")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
