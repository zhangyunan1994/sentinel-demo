package com.example.sentinelmvcdashboard;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping
public class RateLimitController {

    private AtomicLong counter = new AtomicLong(0);

    @GetMapping("retelimit")
    public Map<String, String> rateLimit() {
        long incrementAndGet = counter.incrementAndGet();
        Map<String, String> result = Maps.newHashMap();
        Entry entry = null;
        try {
            entry = SphU.entry("retelimit");
            result.put("result", "success");
        } catch (BlockException e) {
            /*流控逻辑处理 - 开始*/
            System.out.println("block! \t" + incrementAndGet);
            result.put("result", "block");

            /*流控逻辑处理 - 结束*/
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "exception");
            System.out.println("exception! \t" + incrementAndGet);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return result;
    }

    @SentinelResource("rateLimitAlone")
    @GetMapping("rateLimitAlone")
    public Map<String, String> aloneRateLimit() {
        Map<String, String> result = Maps.newHashMap();
        try {
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "exception");
        }
        return result;
    }

    @GetMapping("rateLimitNo")
    public Map<String, String> noRateLimit() {
        long incrementAndGet = counter.incrementAndGet();
        Map<String, String> result = Maps.newHashMap();
        try {
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "exception");
            System.out.println("exception! \t" + incrementAndGet);
        }
        return result;
    }

}
