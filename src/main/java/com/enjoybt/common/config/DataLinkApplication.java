package com.enjoybt.common.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages={"com.enjoybt"})
@EnableScheduling
public class DataLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataLinkApplication.class, args);
    }

}

