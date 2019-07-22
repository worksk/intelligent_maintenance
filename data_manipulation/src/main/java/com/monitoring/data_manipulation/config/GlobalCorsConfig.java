package com.monitoring.data_manipulation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:17
 * @Description: 添加对跨域访问的支持
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        // 重写父类提供的跨域请求处理的接口
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 添加映射路径
                registry.addMapping("/**")
                        // 放行哪些原始域
                        .allowedOrigins("http://127.0.0.1", "http://localhost")
                        // 放行哪些请求方式
                        .allowedMethods("*")
                        .allowCredentials(true)
                        // 放行哪些头部信息
                        .allowedHeaders("*");
            }
        };
    }

}
