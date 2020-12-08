package xyz.itihub.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import xyz.itihub.security.GatewayWebSecurityExpressionHandler;

/**
 * 资源服务器配置
 */
@RequiredArgsConstructor
@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

    private final GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(gatewayWebSecurityExpressionHandler).resourceId("gateway");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .anyRequest().access("#permissionService.hasPermission(request, authentication)");  // 访问控制
    }
}
