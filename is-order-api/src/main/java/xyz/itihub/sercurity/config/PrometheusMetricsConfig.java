package xyz.itihub.sercurity.config;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PrometheusMetricsConfig {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    /**
     * 自定义Counter 统计接口调用次数
     * @return
     */
    @Bean
    public Counter requestCounter(){
        return Counter.build("is_request_count", "count reqyest by service")
                .labelNames("service", "method", "code")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
    }

    @Bean
    public Summary requestLatency(){
        return Summary.build("is_request_latency", "monite_request_latency_by_service")
                .quantile(0.5, 0.05)    // 统计百分之0.5分位数 允许百分之0.05的误差
                .quantile(0.9, 0.01)    // 统计百分之0.9分位数 允许百分之0.01的误差
                .labelNames("service", "method", "code")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
    }
}
