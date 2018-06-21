package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 日志响应信息 </b>
 * <p>
 * 功能描述:返回客户端日志概要信息
 * </p>
 */
@Data
@ApiModel("日志信息")
public class ResLog implements Serializable {


    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty(value = "消息ID")
    private String dataId;

    @ApiModelProperty(value = "模块地址")
    private String serUrl;

    @ApiModelProperty(value = "模块端口")
    private String serPort;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "模块名称")
    private String appSer;

    @ApiModelProperty(value = "接口名称")
    private String interfaceName;

    @ApiModelProperty(value = "数据中心ID")
    private long dataCenterId;

    @ApiModelProperty(value = "工作平台ID")
    private long workerId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "调用类")
    private String classMethod;

    @ApiModelProperty(value = "耗时")
    private long execCount;

    @ApiModelProperty(value = "执行日期")
    private String execDate;

    @ApiModelProperty(value = "执行时间")
    private String execTime;


    public ResLog() {
        super();
    }
}
