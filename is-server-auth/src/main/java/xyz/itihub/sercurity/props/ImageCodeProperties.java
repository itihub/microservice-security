package xyz.itihub.sercurity.props;

import lombok.Getter;
import lombok.Setter;
import xyz.itihub.sercurity.AuthConstant;

@Getter
@Setter
public class ImageCodeProperties {

    /**
     * 图像类型
     */
    private String type = AuthConstant.PNG_IMAGE_FORMAT;

    /**
     * 宽度
     */
    private int width = 130;

    /**
     * 高度
     */
    private int height = 48;

    /**
     * 长度
     */
    private int length = 4;

    /**
     * 过期时间
     */
    private long expireIn = 120L;

    /**
     * 验证码值的类型
     * 1. 数字加字母
     * 2. 纯数字
     * 3. 纯字母
     */
    private Integer charType = 2;

    /**
     * 需要图形验证码的请求路径
     */
    private String url;


}
