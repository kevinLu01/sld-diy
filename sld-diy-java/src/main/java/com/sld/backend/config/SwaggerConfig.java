package com.sld.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/Knife4j 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SLD DIY 商城 API 文档")
                .version("1.0.0")
                .description("生利达冷冻空调配件DIY商城后端接口文档")
                .contact(new Contact()
                    .name("SLD Team")
                    .email("support@sld-mall.com"))
                .license(new License()
                    .name("ISC License")));
    }
}
