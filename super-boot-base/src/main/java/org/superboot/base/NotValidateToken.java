package org.superboot.base;

import java.lang.annotation.*;

/**
 * <b> 如果类不需要TOKEN验证，调用此注解即可 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 13:51
 * @Path org.superboot.base.NotValidateToken
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotValidateToken {
}
