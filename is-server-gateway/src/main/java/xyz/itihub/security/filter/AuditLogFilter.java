package xyz.itihub.security.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.itihub.security.auditlog.LogEntry;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 审计日志过滤器
 */
@Slf4j
@Component
public class AuditLogFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("audit log insert");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        TokenInfo info = (TokenInfo) request.getAttribute("tokenInfo");
        if (info != null){
            String userName = info.getUser_name();
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            LocalDateTime createdTime = LocalDateTime.now();
            LogEntry logEntry = LogEntry.builder()
                    .username(userName)
                    .createdTime(createdTime)
                    .method(method)
                    .path(requestURI)
                    .build();

        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

}
