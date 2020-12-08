package xyz.itihub.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // TODO: 自定义权限控制

        log.info("request uri: {}", request.getRequestURI());
        log.info("authentication: {}", ReflectionToStringBuilder.toString(authentication));

        return RandomUtils.nextInt() % 2 == 0;
    }
}
