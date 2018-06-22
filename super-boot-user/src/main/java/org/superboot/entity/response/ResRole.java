package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 角色响应信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("角色响应信息")
public class ResRole implements Serializable {

    @ApiModelProperty("角色主键")
    private Long pkRole;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色说明")
    private String roleInfo;

    @ApiModelProperty("角色类别 0为系统创建 1为组织创建 ")
    private Integer roleType;

    @ApiModelProperty("隶属组织主键")
    private Long pkGroup;

}
