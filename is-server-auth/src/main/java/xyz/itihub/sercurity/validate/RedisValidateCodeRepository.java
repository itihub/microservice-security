package xyz.itihub.sercurity.validate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.AuthConstant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {


    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        Duration duration = Duration.between(LocalDateTime.now(), validateCode.getExpireTime());
        redisTemplate.opsForValue().set(buildKey(request, validateCodeType)
                , validateCode, 1, TimeUnit.HOURS);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        if (value == null){
            return null;
        }
        return (ValidateCode) value;
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request, validateCodeType));
    }

    private String buildKey(ServletWebRequest request, ValidateCodeType type){
        String key = null;
        switch (type){
            case IMAGE:
                key = request.getParameter(AuthConstant.DEFAULT_PARAMETER_NAME_IMAGE);
                break;
            case SMS:
                key = request.getParameter(AuthConstant.DEFAULT_PARAMETER_NAME_MOBILE);
                break;
            default:
                break;
        }
        return String.format("captcha:%s:%s", type.toString().toLowerCase(), key);
    }
}
