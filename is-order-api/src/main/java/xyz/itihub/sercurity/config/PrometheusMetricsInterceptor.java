package xyz.itihub.sercurity.config;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prometheus统计接口调用次数拦截器
 */
@RequiredArgsConstructor
@Component
public class PrometheusMetricsInterceptor extends HandlerInterceptorAdapter {

    private final Counter requestCounter;

    private final Summary requestLatency;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String service = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        requestCounter.labels(service, method, String.valueOf(status))
                .inc();

        long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
        requestLatency.labels(service, method, String.valueOf(status))
                .observe(duration);
    }


}
