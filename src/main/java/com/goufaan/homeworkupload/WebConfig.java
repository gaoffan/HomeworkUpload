package com.goufaan.homeworkupload;

import com.goufaan.homeworkupload.Interceptor.AuthInterceptor;
import com.goufaan.homeworkupload.Interceptor.CORSInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootConfiguration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    AuthInterceptor authInterceptor;

    @Autowired
    CORSInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/auth/**");
        registry.addInterceptor(corsInterceptor);
    }
}