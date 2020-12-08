package xyz.itihub.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import xyz.itihub.security.auditlog.GatewayAuditLogFilter;

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
        http
                .addFilterBefore(new GatewayAuditLogFilter(), ExceptionTranslationFilter.class) // 添加自定义过滤器
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
//                .anyRequest().authenticated();    // 所有请求经过认证即可访问
                .anyRequest().access("#permissionService.hasPermission(request, authentication)");  // 访问控制

    }
}
