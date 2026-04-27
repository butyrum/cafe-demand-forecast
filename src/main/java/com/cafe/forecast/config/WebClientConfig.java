package com.cafe.forecast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient mogoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("${mogo.api.url:https://api.mogogourmet.com.br}")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public WebClient clickUpWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("${clickup.api.url:https://api.clickup.com/api/v2}")
                .defaultHeader("Authorization", "${clickup.api.token}")
                .build();
    }

    @Bean
    public WebClient ollamaWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("${ollama.api.url:http://localhost:11434}")
                .build();
    }
}
