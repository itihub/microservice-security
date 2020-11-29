package xyz.itihub.security.filter;

import com.lambdaworks.crypto.SCryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.itihub.security.user.User;
import xyz.itihub.security.user.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 身份认证过滤器
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Order(2)
public class BasicAuthecationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        log.info("2.Request to basic authecation filter");

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isNoneBlank(authHeader)){
            String tokenBase64 = StringUtils.substringAfter(authHeader, "Basic ");
            String token = new String(Base64Utils.decodeFromString(tokenBase64));
            String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, ":");

            String username = items[0];
            String password = items[1];

            User user = userRepository.findByUsername(username);

            if (user != null && SCryptUtil.check(password, user.getPassword())){
                request.getSession().setAttribute("user", user.buildInfo());
                request.getSession().setAttribute("temp", Boolean.TRUE);
            }
        }

        try {

            filterChain.doFilter(request, response);

        } finally {

            HttpSession session = request.getSession();
            if (session.getAttribute("temp") != null){
                session.invalidate();
            }
        }

    }

}
