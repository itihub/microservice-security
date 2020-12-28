package xyz.itihub.sercurity.props;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "xyz.security")
public class SecurityProperties {

    public ValidateCodeProperties code = new ValidateCodeProperties();

}
