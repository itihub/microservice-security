package xyz.itihub.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * 自定义身份认证
 * 与默认 OAuth2AuthenticationManager 处理逻辑相同，可以加入一些自定义逻辑进去
 */
@Slf4j
//@Component
public class GatewayAuthenticationManager implements AuthenticationManager {

    @Autowired
    private ResourceServerTokenServices tokenServices;
    @Autowired(required = false)
    private ClientDetailsService clientDetailsService;
    @Value("${security.oauth2.client.client-id:oauth2-resource}")
    private String resourceId;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InvalidTokenException("Invalid token (token not found)");
        } else {
            String token = (String)authentication.getPrincipal();
            OAuth2Authentication auth = this.tokenServices.loadAuthentication(token);
            if (auth == null) {
                throw new InvalidTokenException("Invalid token: " + token);
            } else {
                Collection<String> resourceIds = auth.getOAuth2Request().getResourceIds();
                if (this.resourceId != null && resourceIds != null && !resourceIds.isEmpty() && !resourceIds.contains(this.resourceId)) {
                    throw new OAuth2AccessDeniedException("Invalid token does not contain resource id (" + this.resourceId + ")");
                } else {
                    this.checkClientDetails(auth);
                    if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
                        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
                        if (!details.equals(auth.getDetails())) {
                            details.setDecodedDetails(auth.getDetails());
                        }
                    }

                    auth.setDetails(authentication.getDetails());
                    auth.setAuthenticated(true);
                    return auth;
                }
            }
        }
    }


    private void checkClientDetails(OAuth2Authentication auth) {
        if (this.clientDetailsService != null) {
            ClientDetails client;
            try {
                client = this.clientDetailsService.loadClientByClientId(auth.getOAuth2Request().getClientId());
            } catch (ClientRegistrationException var6) {
                throw new OAuth2AccessDeniedException("Invalid token contains invalid client id");
            }

            Set<String> allowed = client.getScope();
            Iterator var4 = auth.getOAuth2Request().getScope().iterator();

            while(var4.hasNext()) {
                String scope = (String)var4.next();
                if (!allowed.contains(scope)) {
                    throw new OAuth2AccessDeniedException("Invalid token contains disallowed scope (" + scope + ") for this client");
                }
            }
        }

    }
}
