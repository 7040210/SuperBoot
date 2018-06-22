package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b> 组织注册类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("组织请求实体信息")
public class ReqGroup extends BaseRequest {

    @ApiModelProperty("组织编码")
    @NotBlank
    private String groupCode;


    @ApiModelProperty("组织名称")
    @NotBlank
    private String groupName;

    @ApiModelProperty("组织类型，0为 自建、1为授权、2为其他 数据传输无需加密")
    @NotNull
    private Integer groupType;
}
