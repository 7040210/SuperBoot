package org.superboot.base;

/**
 * <b> 响应状态 </b>
 * <p>
 * 功能描述:定义系统的响应状态
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 15:46
 * @Path org.superboot.base.SuperBootStatus
 */
public enum SuperBootStatus {
    NO(-1, "请求失败"),
    OK(1, "请求成功");

    private final int code;
    private final String message;

    SuperBootStatus(int code, String message) {
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


