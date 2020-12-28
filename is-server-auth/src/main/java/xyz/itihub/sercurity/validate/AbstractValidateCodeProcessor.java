package xyz.itihub.sercurity.validate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    @Autowired
    private ValidateCodeGeneratorHolder validateCodeGeneratorHolder;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void create(ServletWebRequest servletWebRequest) throws Exception {
        C validateCode = this.generate(servletWebRequest);
        this.save(servletWebRequest, validateCode);
        this.send(servletWebRequest, validateCode);
    }

    @Override
    public void validate(ServletWebRequest request) throws ValidateCodeException{
        ValidateCodeType processorType = this.getValidateCodeType(request);

        C codeInSession = (C) validateCodeRepository.get(request, processorType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        //判断是否为空
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        //判断验证码是否为null
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        //判断验证码是否过期
        if (codeInSession.isExpired()) {
            throw new ValidateCodeException("验证码已失效");
        }
        //判断验证码是否匹配
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        validateCodeRepository.remove(request, processorType);
    }


    private C generate(ServletWebRequest request) {
        String type = getValidateCodeType(request).toString().toLowerCase();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorHolder.findValidateCodeGenerator(type);
        return (C) validateCodeGenerator.generate(request);
    }

    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    private void save(ServletWebRequest request, C validateCode) {
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        validateCodeRepository.save(request, code, getValidateCodeType(request));
    }

    protected abstract void send(ServletWebRequest servletWebRequest, C validateCode) throws Exception;

}
