package com.example.sentinelmvcdashboard;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SentinelMvcDashboardApplication {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    public static void main(String[] args) {
        SpringApplication.run(SentinelMvcDashboardApplication.class, args);
        System.setProperty("csp.sentinel.dashboard.server", "127.0.0.1:8088");
        System.setProperty("project.name", "sentinelmvcdashboard");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }


}
