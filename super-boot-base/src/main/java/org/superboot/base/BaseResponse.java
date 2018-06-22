package org.superboot.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 响应消息基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("响应信息")
public class BaseResponse<T> implements Serializable {

    @ApiModelProperty(value = "业务码", required = true)
    private int code;
    @ApiModelProperty(value = "状态码", required = true)
    private int status;
    @ApiModelProperty(value = "数据信息")
    private T data;


    /**
     * 默认构造函数 默认操作为成功
     */
    public BaseResponse() {
        this.code = StatusCode.OK.getCode();
        this.status = BaseStatus.OK.getCode();
    }

    /**
     * 默认构造函数 默认操作为成功
     *
     * @param o 数据信息
     */
    public BaseResponse(T o) {
        this.code = StatusCode.OK.getCode();
        this.status = BaseStatus.OK.getCode();
        this.data = o;
    }

    /**
     * 默认构造函数 默认操作为成功
     *
     * @param sbc 状态信息
     */
    public BaseResponse(StatusCode sbc) {
        this.code = sbc.getCode();
        this.status = BaseStatus.OK.getCode();
    }

    /**
     * 默认构造函数 默认操作为成功
     *
     * @param sbc 状态信息
     * @param o   数据信息
     */
    public BaseResponse(StatusCode sbc, T o) {
        this.code = sbc.getCode();
        this.status = BaseStatus.OK.getCode();
        this.data = o;
    }

    /**
     * 默认构造函数 操作状态自定义
     *
     * @param sbc 状态信息
     * @param sbs 操作信息
     */
    public BaseResponse(StatusCode sbc, BaseStatus sbs) {
        this.code = sbc.getCode();
        this.status = sbs.getCode();
    }

    /**
     * 默认构造函数 操作状态自定义
     *
     * @param sbc 状态信息
     * @param sbs 操作信息
     * @param o   数据信息
     */
    public BaseResponse(StatusCode sbc, BaseStatus sbs, T o) {
        this.code = sbc.getCode();
        this.status = sbs.getCode();
        this.data = o;
    }
}
