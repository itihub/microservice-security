package xyz.itihub.security.auditlog;



import java.util.Objects;

public class LogContext {

    private static final ThreadLocal<LogEntry> currentLog = ThreadLocal.withInitial(() -> null);


    public static void set(LogEntry logEntry){
        currentLog.set(logEntry);
    }

    public static LogEntry get(){
        return currentLog.get();
    }

    public static void remove(){
        currentLog.remove();
    }


    public static void setRemark(String remark){
        LogEntry logEntry = currentLog.get();
        if (Objects.nonNull(logEntry)){
            logEntry.setRemark(remark);
        }
    }

}
