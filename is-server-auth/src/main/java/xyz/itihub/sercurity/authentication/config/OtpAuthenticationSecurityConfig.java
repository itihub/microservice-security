package xyz.itihub.sercurity.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import xyz.itihub.sercurity.authentication.OtpAuthenticationProvider;
import xyz.itihub.sercurity.authentication.OtpAuthenticationtionFilter;
import xyz.itihub.sercurity.authentication.handler.MobileAuthenticationFailureHandle;
import xyz.itihub.sercurity.authentication.handler.MobileAuthenticationSuccessHandle;
import xyz.itihub.sercurity.authentication.mobile.MobileUserDetailsService;

@RequiredArgsConstructor
@Component
public class OtpAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private MobileAuthenticationSuccessHandle mobileAuthenticationSuccessHandle;
    @Autowired
    private MobileAuthenticationFailureHandle mobileAuthenticationFailureHandle;
    @Autowired
    private MobileUserDetailsService userDetailService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        OtpAuthenticationtionFilter otpAuthenticationtionFilter = new OtpAuthenticationtionFilter();
        otpAuthenticationtionFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        otpAuthenticationtionFilter.setAuthenticationSuccessHandler(mobileAuthenticationSuccessHandle);
        otpAuthenticationtionFilter.setAuthenticationFailureHandler(mobileAuthenticationFailureHandle);

        OtpAuthenticationProvider otpAuthenticationProvider = new OtpAuthenticationProvider();
        otpAuthenticationProvider.setUserDetailsService(userDetailService);

        builder.authenticationProvider(otpAuthenticationProvider)
                .addFilterAfter(otpAuthenticationtionFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
