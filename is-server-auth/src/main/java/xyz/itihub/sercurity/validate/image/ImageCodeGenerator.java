package xyz.itihub.sercurity.validate.image;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.AuthConstant;
import xyz.itihub.sercurity.props.ImageCodeProperties;
import xyz.itihub.sercurity.props.SecurityProperties;
import xyz.itihub.sercurity.validate.ValidateCodeGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class ImageCodeGenerator implements ValidateCodeGenerator {

    private final SecurityProperties securityProperties;

    @Override
    public ImageCode generate(ServletWebRequest servletWebRequest) {
        ImageCodeProperties imageCodeProperties = securityProperties.getCode().getImage();

        HttpServletResponse response = servletWebRequest.getResponse();
        HttpServletRequest request = servletWebRequest.getRequest();

        int width = ServletRequestUtils.getIntParameter(request, "width", imageCodeProperties.getWidth());
        int height = ServletRequestUtils.getIntParameter(request, "height", imageCodeProperties.getHeight());
        int length = imageCodeProperties.getLength();

        Captcha captcha;
        if (StringUtils.equalsIgnoreCase(imageCodeProperties.getType(), AuthConstant.GIF_IMAGE_FORMAT)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
            captcha = new GifCaptcha(width, height, length);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            captcha = new SpecCaptcha(width, height, length);
        }
        captcha.setCharType(imageCodeProperties.getCharType());

        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);

        return new ImageCode(captcha, imageCodeProperties.getType(), StringUtils.lowerCase(captcha.text()), imageCodeProperties.getExpireIn());
    }


}
