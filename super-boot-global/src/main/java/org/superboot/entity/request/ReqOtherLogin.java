package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> 第三方授权登陆接口 </b>
 * <p>
 * 功能描述: 提供第三方使用员工ID方式登陆系统
 * </p>
 */
@Data
@ApiModel("外部系统授权登陆")
public class ReqOtherLogin extends BaseRequest {

    @ApiModelProperty("登陆用户KEY")
    @NotBlank
    private String userKey;

}
