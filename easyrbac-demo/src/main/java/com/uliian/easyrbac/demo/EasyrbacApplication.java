package com.uliian.easyrbac.demo;

import com.uliian.easyrbac.config.EnableEasyRbacSso;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
@EnableEasyRbacSso
public class EasyrbacApplication {
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(EasyrbacApplication.class);
        app.addListeners(new ApplicationPidFileWriter("pid.pid"));
        app.run(args);
    }
}
