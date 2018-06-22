package org.superboot.base;

import java.lang.annotation.*;

/**
 * <b> 如果方法不需要TOKEN验证，调用此注解即可 </b>
 * <p>
 * 功能描述:如果调用的接口没通过网关，则此注解必须添加
 * </p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethedNotValidateToken {
}
