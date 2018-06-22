package org.superboot.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.utils.DateUtils;

import java.io.Serializable;

/**
 * <b> Token基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("TOKEN基础信息")
public class BaseToken implements Serializable {


    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "用户主键")
    private long userId;

    @ApiModelProperty(value = "组织主键")
    private long pkGroup;

    @ApiModelProperty(value = "用户角色类型，多角色用逗号隔开，主要用于后期一些管理级操作使用")
    private String userRole;

    @ApiModelProperty(value = "未解析出信息后，存放未解析出的原因")
    private StatusCode errCode;

    @ApiModelProperty(value = "最后登陆日期")
    private String loginDate;

    @ApiModelProperty(value = "最后登陆时间")
    private String loginTime;

    @ApiModelProperty("当前员工，系统用户为空")
    private long pkEmp;

    @ApiModelProperty("当前机构，系统用户为空")
    private long pkOrg;

    @ApiModelProperty(value = "账号停用时间")
    private String endTime;

    @ApiModelProperty(value = "用户姓名")
    private String fullName;

    @ApiModelProperty(value = "员工姓名")
    private String empName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "职务名称")
    private String dutiesName;


    public BaseToken() {
        super();
        this.loginDate = DateUtils.getCurrentDate();
        this.loginTime = DateUtils.getCurrentTime();
    }

    /**
     * 构造函数
     *
     * @param username 用户名
     * @param userid   用户ID
     */
    public BaseToken(String username, long userid) {
        super();
        this.userName = username;
        this.userId = userid;
        this.loginDate = DateUtils.getCurrentDate();
        this.loginTime = DateUtils.getCurrentTime();
    }

}
