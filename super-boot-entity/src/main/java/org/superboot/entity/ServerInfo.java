package org.superboot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <b> 项目共用方法类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/10/17
 * @time
 * @Path org.superboot.entity.ServerInfo
 */
@Data
@ApiModel("服务器信息基础信息")
public class ServerInfo implements Serializable {
    @ApiModelProperty(value = "服务运行地址")
    @NotBlank
    private String url;
    @ApiModelProperty(value = "服务运行端口")
    @NotBlank
    private String port;
    @ApiModelProperty(value = "服务名称")
    @NotBlank
    private String serName;
    @ApiModelProperty(value = "服务运行其他信息，格式为Json")
    private String info;
    @ApiModelProperty(value = "当前服务器时间，格式为  yyyy-mm-dd hh:mm:ss")
    private String time;
}
