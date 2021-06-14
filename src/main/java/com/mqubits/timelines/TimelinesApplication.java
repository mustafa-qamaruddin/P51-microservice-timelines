package com.mqubits.timelines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TimelinesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimelinesApplication.class, args);
    }

}
