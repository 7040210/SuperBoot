package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author zhangshuai
 * @date 2017/11/16
 * @time 13:58
 * @Path cn.phxg.entity.response.ResToken
 */
@Data
@ApiModel("响应Token信息")
public class ResToken implements Serializable {

    @ApiModelProperty("Token信息")
    private String token;

    @ApiModelProperty("用户账户")
    private String userName;

    @ApiModelProperty("用户姓名")
    private String fullName;

    @ApiModelProperty("用户主键")
    private Long userPk;

    @ApiModelProperty("组织主键")
    private Long groupPk;

    @ApiModelProperty("用户类型，-1:系统管理员 0 组织管理员 1 系统用户 2 组织用户 3 开放注册用户")
    private Integer userType;


    @ApiModelProperty("当前员工，系统用户为空")
    private long pkEmp;

    @ApiModelProperty("当前机构，系统用户为空")
    private long pkOrg;

    @ApiModelProperty(value = "最后登陆日期")
    private String loginDate;

    @ApiModelProperty(value = "最后登陆时间")
    private String loginTime;

    @ApiModelProperty(value = "账号停用时间")
    private String endTime;

    @ApiModelProperty(value = "员工姓名")
    private String empName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "职务名称")
    private String dutiesName;


}
