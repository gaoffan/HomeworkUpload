package com.goufaan.homeworkupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HomeworkUploadApplication extends SpringBootServletInitializer {

    // run with war
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HomeworkUploadApplication.class);
    }
    // run with jar
    public static void main(String[] args) {
        SpringApplication.run(HomeworkUploadApplication.class, args);
    }
}
