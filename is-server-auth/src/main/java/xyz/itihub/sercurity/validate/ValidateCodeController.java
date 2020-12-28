package xyz.itihub.sercurity.validate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import xyz.itihub.sercurity.AuthConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ValidateCodeController {

    private final Map<String, ValidateCodeProcessor> validateCodeProcessors;

    @GetMapping(value = "code/sms", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = AuthConstant.DEFAULT_PARAMETER_NAME_MOBILE, required = true) String mobile) throws Exception {
        this.createCode("sms", request, response);
    }

    @GetMapping(value = "code/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createImageCode(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = AuthConstant.DEFAULT_PARAMETER_NAME_IMAGE, required = true) String key) throws Exception {
        this.createCode("image", request, response);
    }

    public void createCode(String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateCodeProcessors.get(type + "CodeProcessor")
                .create(new ServletWebRequest(request, response));

    }
}
