package xyz.itihub.security.log;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepostitory extends JpaSpecificationExecutor<AuditLog>, CrudRepository<AuditLog, Long> {
}
