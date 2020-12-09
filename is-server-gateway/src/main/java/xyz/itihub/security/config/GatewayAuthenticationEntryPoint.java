package xyz.itihub.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义401未授权错误处理 用于日志审计
 *
 * 场景：
 * 1. 未传令牌，网关层鉴权拦截
 * 2. 未传令牌，网关层通过 微服务鉴权拦截
 * 3. 传递无效令牌被拦截
 */
@Slf4j
@Component
public class GatewayAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {



        // 未传递令牌异常
        if (authException instanceof AccessTokenRequiredException){
            log.info("2. update log to 401");

        // 无效令牌
        }else {
            log.info("2. add log 401");
        }

        request.setAttribute("logUpdated", "yes");

        super.commence(request, response, authException);
    }
}
