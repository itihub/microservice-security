package xyz.itihub.sercurity.validate.image;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.AuthConstant;
import xyz.itihub.sercurity.validate.AbstractValidateCodeProcessor;

import javax.servlet.http.HttpServletResponse;

@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    @Override
    protected void send(ServletWebRequest servletWebRequest, ImageCode imageCode) throws Exception {

        HttpServletResponse response = servletWebRequest.getResponse();
        if (StringUtils.equalsIgnoreCase(imageCode.getType(), AuthConstant.GIF_IMAGE_FORMAT)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }

        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);

        imageCode.getImage().out(response.getOutputStream());
    }
}
