package com.sbb.sentinelspring.config;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import java.util.Date;

public class Job {

    public static void echo(String param) {
        System.out.println(param + "\t" + new Date());
    }

    public static void rateLimitEcho(int param) {
        Entry entry = null;
        try {
            entry = SphU.entry(RateLimitConfig.JOB_LIMIT);

            /*您的业务逻辑 - 开始*/
            Job.echo("echo: " + param);
            /*您的业务逻辑 - 结束*/
        } catch (BlockException e) {
            /*流控逻辑处理 - 开始*/
            System.out.println("block! \t" + param);
            /*流控逻辑处理 - 结束*/
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception! \t" + param);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
