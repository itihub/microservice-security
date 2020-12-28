package xyz.itihub.sercurity.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.itihub.sercurity.authentication.mobile.MobileUserDetailsService;

@Slf4j
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private MobileUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        OtpAuthenticationToken authenticationToken = (OtpAuthenticationToken) authentication;

        UserDetails user = userDetailsService.loadUserByMobile((String) authenticationToken.getPrincipal());

        if (user == null){
            throw new InternalAuthenticationServiceException("无法读取用户信息");
        }

        OtpAuthenticationToken authenticationResult = new OtpAuthenticationToken(user, user.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public MobileUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(MobileUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
