package xyz.itihub.sercurity.validate;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {

    void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType);

    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    void remove(ServletWebRequest request, ValidateCodeType validateCodeType);
}
