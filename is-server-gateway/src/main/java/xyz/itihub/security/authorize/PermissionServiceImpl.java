package xyz.itihub.security.authorize;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String user  = (String) authentication.getPrincipal();
        // TODO: 自定义权限控制

        log.info("request uri: [{}] {}", method, requestURI);
        log.info("request user: {}", user);
        log.info("authentication: {}", ReflectionToStringBuilder.toString(authentication));

        // 匿名用户判断
        if (authentication instanceof AnonymousAuthenticationToken){
            throw new AccessTokenRequiredException(null);
        }

        return RandomUtils.nextInt() % 2 == 0;
    }
}
