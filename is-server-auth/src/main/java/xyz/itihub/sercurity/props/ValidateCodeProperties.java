package xyz.itihub.sercurity.props;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateCodeProperties {

    /**
     * 图形验证码 配置属性
     */
    private ImageCodeProperties image = new ImageCodeProperties();

    /**
     * 短信验证码 配置属性
     */
    private SmsCodeProperties sms = new SmsCodeProperties();
}
