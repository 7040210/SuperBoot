package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("角色请求实体信息")
public class ReqRole extends BaseRequest {

    @ApiModelProperty(value = "角色编码")
    @NotBlank
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    @NotBlank
    private String roleName;

    @ApiModelProperty(value = "角色说明")
    private String roleInfo;
}
