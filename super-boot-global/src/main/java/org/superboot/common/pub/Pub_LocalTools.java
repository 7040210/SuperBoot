package org.superboot.common.pub;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * <b> 国际化工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Component
public class Pub_LocalTools {

    @Resource
    private MessageSource messageSource;

    /**
     * @param code ：对应messages配置的key.
     * @return
     */

    public String getMessage(String code) {
        return this.getMessage(code, new Object[]{});

    }

    /**
     * @param code ：对应messages配置的key.
     * @return
     */

    public String getMessage(int code) {
        return this.getMessage(String.valueOf(code), new Object[]{});

    }


    public String getMessage(String code, String defaultMessage) {
        return this.getMessage(code, null, defaultMessage);

    }


    public String getMessage(String code, String defaultMessage, Locale locale) {
        return this.getMessage(code, null, defaultMessage, locale);

    }


    public String getMessage(String code, Locale locale) {
        return this.getMessage(code, null, "", locale);

    }


    /**
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */

    public String getMessage(String code, Object[] args) {
        return this.getMessage(code, args, "");

    }


    public String getMessage(String code, Object[] args, Locale locale) {

        return this.getMessage(code, args, "", locale);

    }


    /**
     * @param code           ：对应messages配置的key.
     * @param args           : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */

    public String getMessage(String code, Object[] args, String defaultMessage) {

        //这里使用比较方便的方法，不依赖request
        Locale locale = LocaleContextHolder.getLocale();
        //使用根据Request里配置的参数获取
        return this.getMessage(code, args, defaultMessage, locale);

    }


    /**
     * 指定语言.
     *
     * @param code
     * @param args
     * @param defaultMessage
     * @param locale
     * @return
     */

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);

    }
}
