package com.cafe.forecast.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Café Demand Forecast API")
                        .version("1.0")
                        .description("Sistema inteligente de previsão de demanda e sugestão de produção para cafeteria")
                        .contact(new Contact()
                                .name("Breno de Jesus")
                                .email("contato@cafeforecast.com")));
    }
}
