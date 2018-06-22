package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b> 机构信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("机构请求信息")
public class ReqOrg extends BaseRequest {

    @NotBlank
    @ApiModelProperty("机构编码")
    private String orgCode;

    @NotBlank
    @ApiModelProperty("机构名称")
    private String orgName;

    @NotNull
    @ApiModelProperty("机构类型 0:集团 1:公司 2:部门  数据传输无需加密")
    private Integer nodetype;

    @ApiModelProperty("上级机构  数据传输无需加密")
    private Long pkFOrg;

    @ApiModelProperty("机构级别  数据传输无需加密")
    private Integer orgLev;

    @ApiModelProperty("排序号")
    private String orderCode;

}
