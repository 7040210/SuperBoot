package org.superboot.entity.request;

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
 */
@Data
@ApiModel("登录基本信息")
public class LoginUser extends BaseRequest {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    private String userCode;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    @Length(min = 6, max = 50)
    private String userPassword;

    public LoginUser() {
        super();
    }

    public LoginUser(String username, String password) {
        this.setUserCode(username);
        this.setUserPassword(password);
    }

}
