package xyz.itihub.sercurity.validate;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class ValidateCodeGeneratorHolder {

    private final Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;


    public ValidateCodeGenerator findValidateCodeGenerator(ValidateCodeType type) {
        return this.findValidateCodeGenerator(type.toString().toLowerCase());
    }

    public ValidateCodeGenerator findValidateCodeGenerator(String type) {
        String suffix = StringUtils.substringAfter(ValidateCodeGenerator.class.getSimpleName(), "Validate");
        String name = type.toLowerCase() + suffix;
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(name);
        if (validateCodeGenerator == null) {
            throw new RuntimeException(String.format("%sCodeGenerator Not Found", name));
        }
        return validateCodeGenerator;
    }
}
