package com.sbb.sentinelspring;

import com.sbb.sentinelspring.config.Job;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {

    @Test
    public void test() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:spring-context.xml");

        for (int i = 0; i < 10000000; ++i) {
            Job.rateLimitEcho(i);
        }
    }

}
