package com.example.sdcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SentinelDemoClusterClientApplication {

    public static void main(String[] args) {
        System.setProperty("csp.sentinel.dashboard.server", "127.0.0.1:8088");
        SpringApplication.run(SentinelDemoClusterClientApplication.class, args);
    }

}
