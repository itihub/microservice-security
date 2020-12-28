package xyz.itihub.sercurity.validate;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class ValidateCodeProcessorHolder {

    private final Map<String, ValidateCodeProcessor> validateCodeProcessorMap;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String suffix = StringUtils.substringAfter(ValidateCodeProcessor.class.getSimpleName(), "Validate");
        String name = type.toLowerCase() + suffix;

        ValidateCodeProcessor processor = validateCodeProcessorMap.get(name);

        if (processor == null) {
            throw new RuntimeException(String.format("%s Not Found", name));
        }
        return processor;
    }
}
