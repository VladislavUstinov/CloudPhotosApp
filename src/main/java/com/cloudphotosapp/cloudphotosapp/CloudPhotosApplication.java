package com.cloudphotosapp.cloudphotosapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CloudPhotosApplication {

    public static void main(String[] args) {
        ApplicationContext ctx =  SpringApplication.run(CloudPhotosApplication.class, args);

    }

}
