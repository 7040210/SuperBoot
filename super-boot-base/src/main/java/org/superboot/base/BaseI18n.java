package org.superboot.base;

/**
 * <b> 国际化统一定义类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public enum BaseI18n {
    zh_CN("zh_CN", "中文"),
    en_US("en_US", "英文");

    private String lang;

    private String massage;

    BaseI18n(String lang, String massage) {
        this.lang = lang;
        this.massage = massage;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
