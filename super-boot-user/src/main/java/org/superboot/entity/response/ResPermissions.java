package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 系统资源信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("系统资源信息")
public class ResPermissions implements Serializable {


    @ApiModelProperty("资源主键")
    private Long pkPermissions;

    @ApiModelProperty("资源编码")
    private String permissionsCode;

    @ApiModelProperty("资源名称")
    private String permissionsName;

    @ApiModelProperty("资源说明")
    private String permissionsInfo;

}
