package xyz.itihub.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import xyz.itihub.security.authorize.PermissionService;

/**
 * 自定义表达式处理
 */
@RequiredArgsConstructor
@Component
public class GatewayWebSecurityExpressionHandler extends OAuth2WebSecurityExpressionHandler {

    private final PermissionService permissionService;

    /**
     * 创建评估上下文
     * 添加表达式与鉴权服务关系，以便于进行鉴权
     * @param authentication
     * @param invocation
     * @return
     */
    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, FilterInvocation invocation) {
        StandardEvaluationContext sec = super.createEvaluationContextInternal(authentication, invocation);
        sec.setVariable("permissionService", permissionService);
        return sec;
    }
}
