package com.sld.backend.security;

import java.lang.annotation.*;

/**
 * 从当前认证上下文中解析用户ID
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUserId {
    boolean required() default true;
}
