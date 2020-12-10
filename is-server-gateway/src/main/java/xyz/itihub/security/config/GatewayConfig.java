package xyz.itihub.security.config;

import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GatewayConfig {

    @Value("${spring.profiles.active:NA}")
    private String activeProfile;

    @Value("${spring.application.name:NA}")
    private String appName;

    @PostConstruct
    public void init() {
        // init structured logging
        // 初始化日志格式 设置日志格式为JSON
        StructLog4J.setFormatter(JsonFormatter.getInstance());

        // global log fields setting
        // 全局日志字段设置
        StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
                "env", activeProfile,
                "service", appName});
    }
}
