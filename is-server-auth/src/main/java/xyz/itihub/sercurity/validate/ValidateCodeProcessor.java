package xyz.itihub.sercurity.validate;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeProcessor {

    void create(ServletWebRequest servletWebRequest) throws Exception;

    void validate(ServletWebRequest servletWebRequest) throws ValidateCodeException;
}
