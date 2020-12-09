package xyz.itihub.security.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限流过滤器
 */
public class GatewayRateLimitFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (rateLimiter.tryAcquire()){
            filterChain.doFilter(request, response);
        }else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write("{\"error\":\"too many request\"}");
            response.getWriter().flush();
            return;
        }
    }
}
