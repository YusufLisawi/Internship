package com.nttdata.beca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI becaOpenApi(){
        return new OpenAPI().
                   info(new Info().title("NTT DATA - BECA API Docs").
                           description("NTT DATA - BECA REST API documentation")
                           .version("v1.0.0"));
    }
}
