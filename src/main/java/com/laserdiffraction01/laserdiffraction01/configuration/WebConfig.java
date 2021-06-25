package com.laserdiffraction01.laserdiffraction01.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry){
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

   /* @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver
    createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();

       // Properties mappings = new Properties();
       // mappings.setProperty("DatabaseException", "databaseError");
      //  mappings.setProperty("InvalidCreditCardException", "creditCardError");

      //  r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("someerror");    // No default
     //   r.setExceptionAttribute("ex");     // Default is "exception"
     //   r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }*/
}
