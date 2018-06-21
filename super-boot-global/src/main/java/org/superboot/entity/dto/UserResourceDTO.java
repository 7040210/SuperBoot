package org.superboot.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 用户菜单信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("用户菜单信息")
public class UserResourceDTO implements Serializable {


    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("角色类型")
    private Integer roleType;

    @ApiModelProperty("菜单编码")
    private String menuCode;
    @ApiModelProperty("菜单名称")
    private String menuName;
    @ApiModelProperty("菜单类型")
    private Integer menuType;
    @ApiModelProperty("内部序号")
    private String orderCode;
    @ApiModelProperty("菜单地址")
    private String menuUrl;

    @ApiModelProperty("权限编码")
    private String permissionsCode;
    @ApiModelProperty("权限名称")
    private String permissionsName;

    @ApiModelProperty("方法名称")
    private String methodName;
    @ApiModelProperty("模块路径")
    private String modulePath;
    @ApiModelProperty("方法路径（类名）")
    private String methodPath;
    @ApiModelProperty("接口地址")
    private String url;
    @ApiModelProperty("微服务实例名称")
    private String moduleId;


    public UserResourceDTO() {
        super();
    }
}
