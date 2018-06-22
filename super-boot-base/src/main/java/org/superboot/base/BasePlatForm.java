package org.superboot.base;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
public enum BasePlatForm {

    PLATFORM_IOS("Ios", "苹果"),
    PLATFORM_ANDROID("Android", "安卓"),
    PLATFORM_WEB("Web", "PC端"),
    PLATFORM_AJAX("Ajax", "AJAX"),
    PLATFORM_OTHER("Other", "其他");

    private String code;

    private String platform;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    BasePlatForm(String code, String platform) {
        this.code = code;
        this.platform = platform;
    }
}
