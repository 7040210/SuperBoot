package org.superboot.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> Token基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 12:56
 * @Path org.superboot.base.BaseToken
 */
@Data
@ApiModel("TOKEN基础信息")
public class BaseToken implements Serializable {


    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户主键")
    private Long userid;


    public BaseToken() {
        super();
    }

    /**
     * 构造函数
     *
     * @param username 用户名
     * @param userid   用户ID
     */
    public BaseToken(String username, Long userid) {
        super();
        this.username = username;
        this.userid = userid;
    }

}
