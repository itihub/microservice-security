package xyz.itihub.sercurity.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sentinel 配置
 */
@Component
public class SentinelConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 流控规则
        FlowRule rule = new FlowRule();
        rule.setResource("createOrder");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(10);

        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        FlowRuleManager.loadRules(rules);


        // 熔断规则
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("createOrder");
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        degradeRule.setCount(10);  // m
        degradeRule.setTimeWindow(10); //熔断持续时间 ms
//        degradeRule.setLimitApp();

        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);

        // 热点规则
        ParamFlowRule getOrderRule = new ParamFlowRule();
        getOrderRule.setResource("getOrder");
        getOrderRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        getOrderRule.setCount(10);
        getOrderRule.setDurationInSec(1); // 时间窗口
        getOrderRule.setParamIdx(0);
        ParamFlowItem paramFlowItem = new ParamFlowItem();
        paramFlowItem.setClassType("long");
        paramFlowItem.setObject("1");
        paramFlowItem.setCount(1);
        getOrderRule.setParamFlowItemList(Arrays.asList(paramFlowItem));

        ParamFlowRuleManager.loadRules(Arrays.asList(getOrderRule));
    }
}
