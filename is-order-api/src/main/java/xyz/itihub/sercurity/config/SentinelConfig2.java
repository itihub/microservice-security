package xyz.itihub.sercurity.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 从zookeeper中拉取规则配置 sentinel
 */
@Component
public class SentinelConfig2 {

    @Value("${sentinel.zookeeper.address}")
    private String zkServer;
    @Value("${sentinel.zookeeper.path}")
    private String zkPath;
    @Value("${spring.application.name}")
    private String appName;

    @PostConstruct
    public void loadRules() {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(zkServer, zkPath + "/" + appName, source ->
            JSON.parseArray(source, FlowRule.class)
        );

        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

    }
}
