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
 *
 */
@Data
@ApiModel("菜单权限对照关系")
public class MenuAuthDTO {
    @NotNull
    @ApiModelProperty("资源权限主键 数据传输无需加密")
    private Long pkPermissions;

    @NotNull
    @ApiModelProperty("菜单主键 数据传输无需加密")
    private Long pkMenu;
}
