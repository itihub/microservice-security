package xyz.itihub.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import xyz.itihub.security.auditlog.GatewayAuditLogFilter;
import xyz.itihub.security.ratelimit.GatewayRateLimitFilter;

/**
 * 资源服务器配置
 */
@RequiredArgsConstructor
@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

    private final GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;

    private final GatewayAccessDeniedHandler gatewayAccessDeniedHandler;

    private final GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;

//    private final GatewayAuthenticationManager gatewayAuthenticationManager;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
//                .authenticationManager(gatewayAuthenticationManager)  // 自定义认证管理器 默认使用OAuth2AuthenticationManager
                .authenticationEntryPoint(gatewayAuthenticationEntryPoint) // 401未认证错误处理
                .accessDeniedHandler(gatewayAccessDeniedHandler)  // 403访问拒绝处理
                .expressionHandler(gatewayWebSecurityExpressionHandler) // 权限校验表达式
                .resourceId("gateway");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new GatewayRateLimitFilter(), SecurityContextPersistenceFilter.class) // 添加到第一个过滤器之前
                .addFilterBefore(new GatewayAuditLogFilter(), ExceptionTranslationFilter.class) // 添加自定义过滤器
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
//                .anyRequest().authenticated();   // 所有请求经过认证即可访问
                .anyRequest().access("#permissionService.hasPermission(request, authentication)");  // 访问控制

    }
}
