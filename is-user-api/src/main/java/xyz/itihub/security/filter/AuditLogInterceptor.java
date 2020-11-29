package xyz.itihub.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.itihub.security.log.AuditLog;
import xyz.itihub.security.log.AuditLogRepostitory;
import xyz.itihub.security.user.User;
import xyz.itihub.security.user.UserInfo;
import xyz.itihub.security.util.ThreadLocalUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 审计拦截器
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuditLogInterceptor implements HandlerInterceptor {

    private final AuditLogRepostitory auditLogRepostitory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        log.info("3.Request to basic audit log interceptor");

        AuditLog log = new AuditLog();
        log.setMethod(request.getMethod());
        log.setPath(request.getRequestURI());
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user != null){
            log.setUsername(user.getUsername());
        }
        auditLogRepostitory.save(log);

        request.setAttribute("auditLogId", log.getId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        Long auditLogId = (Long) request.getAttribute("auditLogId");

        AuditLog log = auditLogRepostitory.findById(auditLogId).get();

        log.setStatus(response.getStatus());

        auditLogRepostitory.save(log);
    }
}
