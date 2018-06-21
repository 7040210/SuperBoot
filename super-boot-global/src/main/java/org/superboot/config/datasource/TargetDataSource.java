package org.superboot.config.datasource;

import java.lang.annotation.*;

/**
 * <b> 数据源注解 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String dataSource() default "";//数据源
}