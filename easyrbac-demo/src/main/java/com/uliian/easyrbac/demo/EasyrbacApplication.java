package com.uliian.easyrbac.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class EasyrbacApplication {
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(EasyrbacApplication.class);
        app.addListeners(new ApplicationPidFileWriter("pid.pid"));
        app.run(args);
    }
}
