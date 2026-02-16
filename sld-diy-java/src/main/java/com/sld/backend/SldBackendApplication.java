package com.sld.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * SLD DIY 后端启动类
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@MapperScan("com.sld.backend.modules.*.mapper")
public class SldBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SldBackendApplication.class, args);
    }
}
