package com.example.webscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebScraperApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebScraperApplication.class, args);
    }
}
