package org.superboot.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b> 添加授权角色注解 </b>
 * <p>
 * 功能描述:此注解主要用于系统中某些接口需要特殊身份进行拦截的操作，如果添加此注解则只有注解配置的角色才可用访问此接口
 * </p>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuthRoles {

    /**
     * 角色列表，多个角色用逗号隔开
     *
     * @return
     */
    String[] roles() default {};
}
