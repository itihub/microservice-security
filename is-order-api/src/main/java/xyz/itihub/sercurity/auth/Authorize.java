package xyz.itihub.sercurity.auth;

import java.lang.annotation.*;

/**
 * 自定义鉴权注解
 * 用于服务之间调用鉴权
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {
    // allowed consumers
    String[] value();
}
