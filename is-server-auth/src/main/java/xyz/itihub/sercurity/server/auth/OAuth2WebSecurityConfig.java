package xyz.itihub.sercurity.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import xyz.itihub.sercurity.AuthConstant;
import xyz.itihub.sercurity.authentication.config.OtpAuthenticationSecurityConfig;
import xyz.itihub.sercurity.validate.config.ValidateCodeAuthenticationSecurityConfig;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final OtpAuthenticationSecurityConfig otpAuthenticationSecurityConfig;

    private final ValidateCodeAuthenticationSecurityConfig validateCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)     // 获取用户信息
                .passwordEncoder(passwordEncoder);    // 比对密码;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(AuthConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, AuthConstant.DEFAULT_VALIDATE_CODE_URL).permitAll()
                .anyRequest().authenticated().and()
            .formLogin().and()
            .httpBasic().and()
            .logout().logoutSuccessHandler(logoutSuccessHandler).and()
            .apply(validateCodeAuthenticationSecurityConfig).and()
            .apply(otpAuthenticationSecurityConfig).and()
            .csrf().disable();
    }

}
