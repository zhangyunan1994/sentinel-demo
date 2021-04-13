package com.sbb.sentinelspring.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class RateLimitConfig {

    public final static String JOB_LIMIT = "Job#Limit";

    @Value("${RateLimit.rank}")
    private int rankRateLimit;

    @PostConstruct
    public void initRule() {
        List<FlowRule> rules = Lists.newArrayList();

        // rank 方法规则配置
        FlowRule rule = new FlowRule();
        rule.setResource(JOB_LIMIT);  // 设置资源名称，这里设置了方法名
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);     // 限流模式：分为 QPS 和 Thread, 这里使用了 QPS
        rule.setCount(rankRateLimit);                   // 限流数量，这里根据配置读取
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 预热，先放过小部分请求，在通过大部分请求

        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
