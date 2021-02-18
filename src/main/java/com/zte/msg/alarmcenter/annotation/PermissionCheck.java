package com.zte.msg.alarmcenter.annotation;

import java.lang.annotation.*;

/**
 * @author frp
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionCheck {

    // 访问所需的权限，默认为空
    // 登录即可访问
    String[] permissionName() default "";

}
