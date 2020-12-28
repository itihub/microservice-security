package xyz.itihub.sercurity.validate.image;

import com.wf.captcha.base.Captcha;
import lombok.Getter;
import lombok.Setter;
import xyz.itihub.sercurity.validate.ValidateCode;


@Getter
@Setter
public class ImageCode extends ValidateCode {

    private Captcha image;

    private String type;

    public ImageCode(Captcha image, String type, String code, long expireIn) {
        super(code, expireIn);
        this.image = image;
        this.type = type;
    }

}
