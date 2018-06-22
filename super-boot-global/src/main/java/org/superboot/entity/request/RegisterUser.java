package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> 用户注册使用的实体对象 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("注册用户基本信息")
public class RegisterUser extends BaseRequest {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    private String userCode;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String userPassword;

    @ApiModelProperty("邮箱")
    private String userEmail;

    @ApiModelProperty("手机号码")
    private String userPhone;

    @ApiModelProperty(value = "姓名", required = true)
    private String userName;

    @ApiModelProperty("停用时间")
    private String endTime;
}
