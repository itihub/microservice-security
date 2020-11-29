package xyz.itihub.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.itihub.security.filter.AclInterceptor;
import xyz.itihub.security.filter.AuditLogInterceptor;
import xyz.itihub.security.user.User;
import xyz.itihub.security.user.UserInfo;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing
public class SecurityConfig implements WebMvcConfigurer {

    private final AuditLogInterceptor auditLogInterceptor;

    private final AclInterceptor aclInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditLogInterceptor);
        registry.addInterceptor(aclInterceptor);
    }

    /**
     * 实现JPA创建人自动填充
     * 这是使用了硬编码，可以使用redis或者ThreadLocal 来存储用户信息，从而解决硬编码问题
     * @return
     */
    @Bean
    public AuditorAware<String> auditorAware(){
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpSession session = requestAttributes.getRequest().getSession();
                UserInfo user = (UserInfo) session.getAttribute("user");
                String username = Optional.ofNullable(user).orElse(new UserInfo()).getUsername();
                return Optional.ofNullable(username);
            }
        };
    }
}
