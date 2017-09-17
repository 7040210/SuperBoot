package org.superboot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * <b> 用户注册使用的实体对象 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/8
 * @time 11:21
 * @Path org.superboot.entity.RegisterUser
 */
@Data
@ApiModel("注册用户基本信息")
public class RegisterUser extends BaseRequest {

    @ApiModelProperty(value = "账号")
    @Size(max = 20)
    @NotBlank
    private String usercode;
    @ApiModelProperty("密码")
    @Size(max = 18, min = 6)
    @NotBlank
    private String password;
    @ApiModelProperty("邮箱")
    @Email
    private String email;
    @ApiModelProperty("手机号码")
    @Size(max = 18, min = 11)
    private String phone;
}
