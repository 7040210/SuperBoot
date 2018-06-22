package org.superboot.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("菜单角色对照")
public class RoleMenuDTO {

    @ApiModelProperty("菜单主键")
    @NotNull
    private Long pkMenu;

    @ApiModelProperty("角色主键 数据传输无需加密")
    private Long pkRole;

}
