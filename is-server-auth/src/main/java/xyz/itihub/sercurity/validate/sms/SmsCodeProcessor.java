package xyz.itihub.sercurity.validate.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.AuthConstant;
import xyz.itihub.sercurity.env.EnvConfig;
import xyz.itihub.sercurity.validate.AbstractValidateCodeProcessor;
import xyz.itihub.sercurity.validate.ValidateCode;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    private final ObjectMapper objectMapper;

    private final EnvConfig envConfig;

    @Override
    protected void send(ServletWebRequest servletWebRequest, ValidateCode validateCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), AuthConstant.DEFAULT_PARAMETER_NAME_MOBILE);
        if (envConfig.isDebug()){
            HttpServletResponse response = servletWebRequest.getResponse();
            Map<String, Object> result = Maps.newHashMap();
            result.put("otp", validateCode.getCode());
            result.put("expireTime", validateCode.getExpireTime());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(objectMapper.writeValueAsString(result));

        }else {
            // TODO: 调用短信服务商发送验证码
        }
    }
}
