package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b> ReqUserEmployees </b>
 * <p>
 * 功能描述:ReqUserEmployees请求实体
 * </p>
 */
@Data
@ApiModel("用户员工关系请求实体基本信息")
public class ReqUserEmployees extends BaseRequest {
    @NotNull
    @ApiModelProperty("员工主键")
    private Long pkEmployees;

    @NotNull
    @ApiModelProperty("用户主键")
    private Long pkUser;
}