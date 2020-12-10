package xyz.itihub.security.auditlog;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 日志过滤器
 */
public class GatewayAuditLogFilter extends OncePerRequestFilter {

    static final ILogger logger = SLoggerFactory.getLogger(GatewayAuditLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // create log
        LogEntry logEntry = new LogEntry();
        logEntry.setCreatedTime(LocalDateTime.now());
        logEntry.setCurrentUserId(user);
        logEntry.setMethod(request.getMethod());
        logEntry.setPath(request.getRequestURI());
        LogContext.set(logEntry);

//        logger.info("1. add log for ", "user", user);

        filterChain.doFilter(request, response);

        if (StringUtils.isBlank((String) request.getAttribute("logUpdated"))){
//            logger.info("3. update log to success");
        }else {
            logEntry = LogContext.get();
        }

        logEntry.setStatus(String.valueOf(response.getStatus()));
        logEntry.setModifyTime(LocalDateTime.now());
        logger.info("request:", logEntry);

        LogContext.remove();
    }
}
