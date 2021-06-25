package com.laserdiffraction01.laserdiffraction01;

import com.laserdiffraction01.laserdiffraction01.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LaserDiffraction01Application {

    public static void main(String[] args) {
        ApplicationContext ctx =  SpringApplication.run(LaserDiffraction01Application.class, args);

    }

}
