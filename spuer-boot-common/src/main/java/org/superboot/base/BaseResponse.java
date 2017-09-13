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
 *
 * @author jesion
 * @date 2017/9/9
 * @time 19:59
 * @Path org.superboot.base.api.BaseBody
 */
@Data
@ApiModel("响应消息基础信息")
public class BaseResponse implements Serializable {

    @ApiModelProperty(value = "状态信息")
    private SuperBootCode superBootCode;
    @ApiModelProperty(value = "操作信息")
    private SuperBootStatus superBootStatus;
    @ApiModelProperty(value = "数据信息")
    private Object data;


    /**
     * 默认构造函数 默认操作为成功
     */
    public BaseResponse() {
        this.superBootCode = SuperBootCode.OK;
        this.superBootStatus = SuperBootStatus.OK;
    }

    /**
     * 默认构造函数 默认操作为成功
     *
     * @param o 数据信息
     */
    public BaseResponse(Object... o) {
        this.superBootCode = SuperBootCode.OK;
        this.superBootStatus = SuperBootStatus.OK;
        this.data = o;
    }

    /**
     * 默认构造函数 默认操作为成功
     *
     * @param sbc 状态信息
     * @param o   数据信息
     */
    public BaseResponse(SuperBootCode sbc, Object... o) {
        this.superBootCode = sbc;
        this.superBootStatus = SuperBootStatus.OK;
        this.data = o;
    }

    /**
     * 默认构造函数 操作状态自定义
     *
     * @param sbc 状态信息
     * @param sbs 操作信息
     * @param o   数据信息
     */
    public BaseResponse(SuperBootCode sbc, SuperBootStatus sbs, Object... o) {
        this.superBootCode = sbc;
        this.superBootStatus = sbs;
        this.data = o;
    }
}
