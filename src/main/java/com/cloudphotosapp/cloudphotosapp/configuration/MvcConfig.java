package com.cloudphotosapp.cloudphotosapp.configuration;

import com.cloudphotosapp.cloudphotosapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/news").setViewName("news");
        registry.addViewController("/error").setViewName("errors/404error");

        log.debug ("MvcConfig.addViewControllers(registry) has worked fine");
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
