package org.superboot.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b> TreeProperty </b>
 * <p>
 * 功能描述:树节点字段属性注解服务
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TreeProperty {
    TreeField value() default TreeField.ID;
}
