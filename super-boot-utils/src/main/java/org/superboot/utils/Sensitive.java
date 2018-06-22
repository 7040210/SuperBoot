package org.superboot.utils;

import java.lang.annotation.*;

/**
 * <b> 定义字符脱敏规则 </b>
 * <p>
 * 功能描述:定义字符的脱敏规则，规则参考 @SensitiveType ,其中需要使用地址规则的时候 需要指定endLength信息，自定义规则需要同时指定beginLength 与 endLength
 * </p>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Sensitive {
    /**
     * 规则类型 参考 @SensitiveType
     *
     * @return
     */
    SensitiveType type();

    /**
     * 前面显示位数
     *
     * @return
     */
    int beginLength() default 0;

    /**
     * 在地址脱敏的时候，此字段为后面隐藏位数，自定义规则的时候为后面显示位数
     *
     * @return
     */
    int endLength() default 0;
}
