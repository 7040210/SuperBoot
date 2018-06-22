package org.superboot.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 基础消息返回对象 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("基础消息返回对象")
public class BaseMessage implements Serializable {


    @ApiModelProperty(value = "返回码")
    private int code;
    @ApiModelProperty(value = "返回状态，1为成功 -1为失败")
    private int status;
    @ApiModelProperty(value = "返回消息，如请求异常此处会包含异常信息")
    private String message;
    @ApiModelProperty(value = "数据体")
    private Object data;

    public BaseMessage() {
        super();
    }

    public BaseMessage(Throwable e) {
        super();
        this.code = StatusCode.NO.getCode();
        this.status = BaseStatus.NO.getCode();
        this.message = e.getMessage();
    }

}
