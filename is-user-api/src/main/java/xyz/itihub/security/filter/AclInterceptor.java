package xyz.itihub.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.itihub.security.user.User;
import xyz.itihub.security.user.UserInfo;
import xyz.itihub.security.util.ThreadLocalUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器
 */
@Slf4j
@Component
public class AclInterceptor implements HandlerInterceptor {

    private final String[] permitUrls = new String[]{"/users/login"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        log.info("4.Request to basic acl interceptor");

        boolean result = true;

        if (!ArrayUtils.contains(permitUrls, request.getRequestURI())) {

            UserInfo user = (UserInfo) request.getSession().getAttribute("user");

            if (user == null){
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("need authentication");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                result = false;
            }else {
                String method = request.getMethod();
                if (!user.hasPermission(method)) {
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().write("forbidden");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    result = false;
                }
            }
        }


        return result;
    }
}
