package xyz.itihub.sercurity.validate.sms;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.props.SecurityProperties;
import xyz.itihub.sercurity.validate.ValidateCode;
import xyz.itihub.sercurity.validate.ValidateCodeGenerator;

@RequiredArgsConstructor
@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {

    private final SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest servletWebRequest) {
        String code = RandomStringUtils.randomNumeric(
                securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
