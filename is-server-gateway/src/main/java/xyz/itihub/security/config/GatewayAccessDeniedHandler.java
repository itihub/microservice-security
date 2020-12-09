package xyz.itihub.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义403 错误处理 用于日志审计
 */
@Slf4j
@Component
public class GatewayAccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {

        log.info("2. update log to 403");

        request.setAttribute("logUpdated", "yes");

        super.handle(request, response, authException);
    }
}
