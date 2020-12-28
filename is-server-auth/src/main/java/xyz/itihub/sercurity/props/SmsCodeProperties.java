package xyz.itihub.sercurity.props;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsCodeProperties {

    /**
     * 长度
     */
    private int length = 6;

    /**
     * 过期时间
     */
    private long expireIn = 120L;

    /**
     * 需要短信验证码的请求路径
     */
    private String url;


}
