package xyz.itihub.security.config;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.itihub.security.auditlog.LogContext;
import xyz.itihub.security.auditlog.LogEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 自定义401未授权错误处理 用于日志审计
 *
 * 场景：
 * 1. 未传令牌，网关层鉴权拦截
 * 2. 未传令牌，网关层通过 微服务鉴权拦截
 * 3. 传递无效令牌被拦截
 */
@Component
public class GatewayAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    static final ILogger logger = SLoggerFactory.getLogger(GatewayAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {



        // 未传递令牌异常
        if (authException instanceof AccessTokenRequiredException){
//            logger.info("2. update log to 401");
            LogContext.setRemark("unauthorized");
        // 无效令牌
        }else {
//            logger.info("2. add log 401");

            LogEntry logEntry = new LogEntry();
            logEntry.setCreatedTime(LocalDateTime.now());
            logEntry.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            logEntry.setRemark("Illegal Authorization");
            logEntry.setMethod(request.getMethod());
            logEntry.setPath(request.getRequestURI());
            logEntry.setModifyTime(LocalDateTime.now());
            logger.info("request:", logEntry);
        }

        request.setAttribute("logUpdated", "yes");

        super.commence(request, response, authException);
    }
}
