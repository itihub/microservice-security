package xyz.itihub.security.auditlog;

import com.github.structlog4j.IToLog;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 定义结构化日志数据格式
 * 方便审计日志
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry implements IToLog {

    private String currentUserId;
    private LocalDateTime createdTime;
    private LocalDateTime modifyTime;
    private String method;
    private String path;
    private String status;
    private String username;

    @Override
    public Object[] toLog() {
        return new Object[] {
                "auditlog", "true",
                "currentUserId", currentUserId,
                "createdTime", createdTime,
                "modifyTime", modifyTime,
                "method", method,
                "path", path,
                "status", status,
                "username", username
        };
    }
}
