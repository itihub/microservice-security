package xyz.itihub.sercurity.auth;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 控制器授权截获机制
 * 拦截@Authorize 标注的方法，来进行内部微服务的权限控制
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Authorize authorize = handlerMethod.getMethod().getAnnotation(Authorize.class);
        if (authorize == null) {
            return true; // no need to authorize
        }

        String[] allowedHeaders = authorize.value();
        String authHeader = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);

        // 未传递令牌
        if (StringUtils.isEmpty(authHeader)) {
            throw new PermissionDeniedException("unauthorized service!");
        }

        // 权限不足
        if (!Arrays.asList(allowedHeaders).contains(authHeader)) {
            throw new PermissionDeniedException("Service Permission denied!");
        }

        return true;
    }
}
