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
@ApiModel("角色用户对照")
public class RoleUserDTO {

    @ApiModelProperty("角色主键")
    @NotNull
    private Long pkRole;
    @ApiModelProperty("用户主键")
    @NotNull
    private Long pkUser;
}
