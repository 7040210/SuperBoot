package org.superboot.base;

import lombok.Data;

/**
 * <b> 平台统一使用异常类便于统一抓取 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/12
 * @time 15:48
 * @Path org.superboot.base.SuperBootException
 */
@Data
public class SuperBootException extends RuntimeException {

    private int code;

    private String message;


    public SuperBootException(int code ,String message){
        super(message);
        this.code = code;
        this.message = message;
    }


    public SuperBootException(SuperBootCode sc){
        super(sc.getMessage());
        this.code = sc.getCode();
        this.message = sc.getMessage();
    }

    public SuperBootException(int code ,String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public SuperBootException(SuperBootCode sc, Throwable t){
        super(sc.getMessage(),t);
        this.code = sc.getCode();
        this.message = sc.getMessage();
    }
}
