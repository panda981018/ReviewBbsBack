package com.back.review;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableBatchProcessing
@EnableRedisHttpSession
@SpringBootApplication
public class ReviewBbsBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewBbsBackApplication.class, args);
    }

}
