package com.icicles.wmms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableSwagger2
@EnableAuthorizationServer
@MapperScan("com.icicles.wmms.DAO")
public class WmmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmmsApplication.class, args);
    }

}
