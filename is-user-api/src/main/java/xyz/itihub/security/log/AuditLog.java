package xyz.itihub.security.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private String path;

    private Integer status;

    @CreatedBy
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifyTime;
}
