package xyz.itihub.sercurity.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.sql.DataSource;

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

    private UserDetailServiceImpl userDetailService;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer()  );
    }

    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 进行签名，防止篡改
//        converter.setSigningKey("123456");    // 字符串加密
        // 签名加密
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jizhe.key"), "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jizhe"));
        return converter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()")    //暴露token key
                .checkTokenAccess("isAuthenticated()");
    }

    // 客户端配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
//        // 存储到内存种
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


    // 用户
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .userDetailsService(userDetailService)  // 支持refresh_token模式
                .tokenStore(tokenStore())
                .tokenEnhancer(jwtTokenEnhancer())
                .authenticationManager(authenticationManager); // 支持前四种模式
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
