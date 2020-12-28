package xyz.itihub.sercurity.authentication.mobile;

import org.springframework.security.core.userdetails.UserDetails;

public interface MobileUserDetails extends UserDetails {

    String getMobile();
}
