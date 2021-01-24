package com.kgh.serverstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//
//@ComponentScan
//@SpringBootConfiguration
//@EnableAutoConfiguration -> 동작을 하는 이유다. Tomcat
public class ServerStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerStudyApplication.class, args);
    }

}
