package com.enjoybt.common.config;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorPageConfiguration{

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error"));
        factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"));
        factory.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error"));
        factory.addErrorPages(new ErrorPage(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "/error"));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
        return factory;
    }
}
