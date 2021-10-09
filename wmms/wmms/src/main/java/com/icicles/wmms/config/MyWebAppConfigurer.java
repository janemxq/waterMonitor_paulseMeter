package com.icicles.wmms.config;

import com.icicles.wmms.filter.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
/**
 * 请求过滤规则
 * @author lisy
 */
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Bean
    public Interceptor Interceptor() {
        return new Interceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //判断所有操作
        InterceptorRegistration ir=registry.addInterceptor(Interceptor());
        ir.addPathPatterns("/**");


        //不需要过滤的
        List<String> noAccess = new ArrayList<>();

        //静态文件
        noAccess.add("/static/**");
        noAccess.add("/webjars/**");
        //swagger
        noAccess.add("/swagger-ui.html");
        noAccess.add("/swagger-resources/**");
        noAccess.add("/webjars/**");
        ir.excludePathPatterns(noAccess);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
