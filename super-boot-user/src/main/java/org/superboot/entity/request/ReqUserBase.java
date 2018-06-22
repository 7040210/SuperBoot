package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("用户基础信息变更内容")
public class ReqUserBase extends BaseRequest {

    @NotBlank
    @ApiModelProperty(value = "用户姓名，如不变更则保持原来的不变", required = true)
    private String userName;

    @ApiModelProperty(value = "账号失效时间，如果传空则账号永不失效")
    private String endTime;

    @NotNull
    @ApiModelProperty(value = "用户状态，0 正常 1 锁定", required = true)
    private Integer userStatus;

}
