package xyz.itihub.sercurity.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import xyz.itihub.sercurity.server.auth.jwt.JwtTokenEnhancer;

/**
 * 令牌存储方式配置
 *  支持
 *      redis 存储  / JWT 令牌
 */
@Configuration
public class TokenStoreConfig {

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @ConditionalOnProperty(prefix = "security.oauth2", name = "store-type", havingValue = "redis", matchIfMissing = true)
    public TokenStore tokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "security.oauth2", name = "store-type", havingValue = "jwt")
    public static class JwtTokenConfig{


        /**
         * jwt token存储处理
         */
        @Bean
        public TokenStore tokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * token生成处理
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            // 进行签名，防止篡改
//        converter.setSigningKey("123456");    // 字符串加密
            // 签名加密
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jizhe.key"), "123456".toCharArray());
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jizhe"));
            return converter;
        }

        /**
         * 支持扩展
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer(){
            return new JwtTokenEnhancer();
        }
    }
}
