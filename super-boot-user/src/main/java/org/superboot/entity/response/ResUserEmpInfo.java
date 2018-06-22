package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> ResUserEmpInfo </b>
 * <p>
 * 功能描述:ResUserEmpInfo响应实体
 * </p>
 *
 * @author wangrui
 * @date 2018/2/6
 * @time 14:45
 * @path cn.phxg.entity.response.ResUserEmpInfo
 */
@Data
@ApiModel("ResUserEmpInfo响应实体基本信息")
public class ResUserEmpInfo implements Serializable {

    @ApiModelProperty("用户员工主键")
    private Long pkUserEmployees;

    @ApiModelProperty("用户主键")
    private Long pkUser;

    @ApiModelProperty("用户账户")
    private String userCode;

    @ApiModelProperty("用户邮箱")
    private String userEmail;

    @ApiModelProperty("用户手机")
    private String userPhone;

    @ApiModelProperty("用户状态 0正常 1锁定")
    private Integer userStatus;

    @ApiModelProperty("用户姓名")
    private String userName;

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


    @ApiModelProperty("员工主键")
    private Long pkEmployees;

    @ApiModelProperty("员工编码")
    private String employeesCode;

    @ApiModelProperty("员工名称")
    private String employeesName;

    public ResUserEmpInfo() {
        super();
    }
}