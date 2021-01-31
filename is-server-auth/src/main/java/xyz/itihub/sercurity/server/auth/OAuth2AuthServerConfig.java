package xyz.itihub.sercurity.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import xyz.itihub.sercurity.server.auth.jwt.JwtTokenEnhancer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OAuth授权服务器
 */
@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer
@EnableJdbcHttpSession
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final DataSource dataSource;

    private UserDetailsService userDetailService;

    @Autowired(required = false)
    private TokenStore tokenStore;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()")    //暴露token_key获取令牌接口访问
                .checkTokenAccess("isAuthenticated()");  //暴露check_token校验令牌接口访问
    }

    @Autowired(required = false)
    private CacheClientDetailsService cacheClientDetailsService;

    // 客户端配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        if (Objects.nonNull(cacheClientDetailsService)) {
            // 存储到缓存
            clients.withClientDetails(cacheClientDetailsService);
        } else {
            // 存储到JDBC
            clients.jdbc(dataSource);
        }

//        // 存储到内存中
//        clients.inMemory()
//                .withClient("orderApp")
//                .secret(passwordEncoder.encode("123456"))
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(3600) // 令牌有效期
//                .resourceIds("order-server")  // 允许访问资源服务ID
//                .authorizedGrantTypes("password")   // 授权方式
//                .and()
//                .withClient("orderService")
//                .secret(passwordEncoder.encode("123456"))
//                .scopes("read")
//                .accessTokenValiditySeconds(3600) // 令牌有效期
//                .resourceIds("order-server")  // 允许访问资源服务ID
//                .authorizedGrantTypes("password")   // 授权方式
//        ;
    }


    // 用户 授权服务器配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .userDetailsService(userDetailService)  // 支持refresh_token模式
                .authenticationManager(authenticationManager); // 支持前四种模式

        if (Objects.nonNull(tokenStore)) {
            endpoints.tokenStore(tokenStore);
        }

        // jwt 令牌
        if (Objects.nonNull(jwtTokenEnhancer) && Objects.nonNull(jwtAccessTokenConverter)) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints
                    .tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
