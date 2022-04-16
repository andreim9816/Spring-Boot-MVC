package com.example.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class MvcConfiguration {

//    @Bean(name = "simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver getSimpleMappingExceptionResolver() {
//
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//
//        r.setDefaultErrorView("error_default");
//        r.setExceptionAttribute("exception");
//
//        return r;
//    }

    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver getSimpleMappingExceptionResolver() {

        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("NumberFormatException", "numberformaterr");
        r.setExceptionMappings(mappings);

        Properties statusCodes = new Properties();
        statusCodes.setProperty("NumberFormatException", "400");
        r.setStatusCodes(statusCodes);

        return r;
    }
}
