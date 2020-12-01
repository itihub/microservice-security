package xyz.itihub.security.ratelimit;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义触发限流错误处理
 */
@Slf4j
@Component
public class MyRateLimitErrorHandler extends DefaultRateLimiterErrorHandler {

    @Override
    public void handleError(String msg, Exception e) {
        // TODO: 自定义限流错误错误处理
        log.info("capture rate limit error, msg:{}", msg);
        super.handleError(msg, e);

    }
}
