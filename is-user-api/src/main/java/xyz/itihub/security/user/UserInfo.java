package xyz.itihub.security.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInfo {

    private Long id;

    private String name;

    private String username;
    
    private String password;

    private String permissions;

    public boolean hasPermission(String method){
        boolean result = false;
        if (StringUtils.equalsIgnoreCase("get", method)){
            result = StringUtils.contains(permissions, "r");
        }else {
            result = StringUtils.contains(permissions, "w");
        }
        return result;
    }
}
