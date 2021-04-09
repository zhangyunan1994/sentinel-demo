package com.sbb.sentineldemo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SentinelStaticQPSRuleTest {

    @Before
    public void initRule() {
        List<FlowRule> rules = Lists.newArrayList();
        FlowRule rule = new FlowRule();
        rule.setResource("qps20");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(1000);
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 超出的直接拒绝
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP); // 预热，先放过小部分请求，在通过大部分请求
        rule.setWarmUpPeriodSec(10);
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }


    @Test
    public void testQPS20Block() {
        for (int i = 0; ; ++i) {
            Entry entry = null;
            try {
                entry = SphU.entry("qps20");

                /*您的业务逻辑 - 开始*/
                Job.echo("echo: " + i);
                /*您的业务逻辑 - 结束*/
            } catch (BlockException e) {
                /*流控逻辑处理 - 开始*/
                System.out.println("block! \t" + i);
                /*流控逻辑处理 - 结束*/
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }


}
