package xyz.itihub.security.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String permissions;

    public UserInfo buildInfo(){
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(this, info);
        return info;
    }


}
