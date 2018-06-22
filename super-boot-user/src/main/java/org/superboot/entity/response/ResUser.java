package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.utils.Sensitive;
import org.superboot.utils.SensitiveType;

import java.io.Serializable;

/**
 * <b> 用户基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("用户响应实体基础信息")
public class ResUser implements Serializable {

    @ApiModelProperty("用户主键")
    private Long pkUser;

    @ApiModelProperty("用户账户")
    private String userCode;

    @Sensitive(type = SensitiveType.EMAIL)
    @ApiModelProperty("用户邮箱")
    private String userEmail;

    @Sensitive(type = SensitiveType.MOBILE_PHONE)
    @ApiModelProperty("用户手机")
    private String userPhone;

    @ApiModelProperty("用户状态 0正常 1锁定")
    private Integer userStatus;

    @Sensitive(type = SensitiveType.CHINESE_NAME)
    @ApiModelProperty("用户姓名")
    private String userName;

    @Sensitive(type = SensitiveType.ID_CARD)
    @ApiModelProperty("用户身份证号")
    private String userId;

    @ApiModelProperty("认证状态 0为注册用户 1为认证用户")
    private Integer userAuth;

    @ApiModelProperty("是否为初始用户，默认0为初始用户 1为非初始用户，初始用户需要初始化信息")
    private Integer initUser;

    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("组织名称")
    private String groupName;
    @ApiModelProperty("组织编码")
    private String groupCode;


    @ApiModelProperty("停用时间")
    private String endTime;

    public ResUser() {
        super();
    }
}