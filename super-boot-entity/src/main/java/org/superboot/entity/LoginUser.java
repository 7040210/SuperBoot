package org.superboot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> 用户登录 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/12
 * @time 13:27
 * @Path org.superboot.entity.LoginUser
 */
@Data
@ApiModel("登录基本信息")
public class LoginUser extends BaseRequest {

    @ApiModelProperty(value = "账号")
    @NotBlank
    private String username;
    @ApiModelProperty(value = "密码")
    @NotBlank
    @Length(min=6,max=20)
    private String password;

    public LoginUser() {
        super();
    }

    public LoginUser(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

}
