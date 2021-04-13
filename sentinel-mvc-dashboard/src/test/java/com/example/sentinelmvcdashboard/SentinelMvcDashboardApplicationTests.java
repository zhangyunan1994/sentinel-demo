package com.example.sentinelmvcdashboard;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class SentinelMvcDashboardApplicationTests {

    @Test
    void contextLoads() {

        OkHttpClient client = new OkHttpClient();

        ConcurrentRunner concurrentRunner = new ConcurrentRunner(10, 10, () -> {
            //创建一个Request
            Request request = new Request.Builder()
                    .get()
                    .url("http://localhost:10888/rateLimitAlone")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        concurrentRunner.simulateTraffic();
        concurrentRunner.tick();
    }

    @Test
    void contextLoadsReteLimit() {

        OkHttpClient client = new OkHttpClient();

        ConcurrentRunner concurrentRunner = new ConcurrentRunner(10, 10, () -> {
            //创建一个Request
            Request request = new Request.Builder()
                    .get()
                    .url("http://localhost:10888/retelimit")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        concurrentRunner.simulateTraffic();
        concurrentRunner.tick();
    }

}
