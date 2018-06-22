package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> 修改密码信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */

@Data
@ApiModel("修改密码请求实体信息")
public class ReqPassWord extends BaseRequest {

    @ApiModelProperty(value = "旧密码")
    @NotBlank
    private String oldpassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank
    @Length(min = 6)
    private String password;
}
