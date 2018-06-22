package org.superboot.base;

import lombok.Data;

/**
 * <b> 平台统一使用异常类便于统一抓取 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
public class BaseException extends RuntimeException {

    private int code;

    private String message;


    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }


    public BaseException(StatusCode sc) {
        super(sc.getMessage());
        this.code = sc.getCode();
        this.message = sc.getMessage();
    }

    public BaseException(StatusCode sc, String message) {
        super(message);
        this.code = sc.getCode();
        this.message = message;
    }


    public BaseException(int code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public BaseException(StatusCode sc, Throwable t) {
        super(sc.getMessage(), t);
        this.code = sc.getCode();
        this.message = sc.getMessage();
    }


}
