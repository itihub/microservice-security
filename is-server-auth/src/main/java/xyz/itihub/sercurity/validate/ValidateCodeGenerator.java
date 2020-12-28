package xyz.itihub.sercurity.validate;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {

    ValidateCode generate(ServletWebRequest servletWebRequest);
}
