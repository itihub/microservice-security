package xyz.itihub.sercurity.validate;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码类型异常
 *
 * @author walker
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 7514854456967620043L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
