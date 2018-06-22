package org.superboot.base;

/**
 * <b> 响应状态 </b>
 * <p>
 * 功能描述:定义系统的响应状态
 * </p>
 */
public enum BaseStatus {
    NO(-1, "请求失败"),
    OK(1, "请求成功");

    private final int code;
    private final String message;

    BaseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
