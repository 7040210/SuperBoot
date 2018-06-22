package org.superboot.base;

import java.lang.annotation.*;

/**
 * <b> 如果类不需要TOKEN验证，调用此注解即可 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotValidateToken {
}
