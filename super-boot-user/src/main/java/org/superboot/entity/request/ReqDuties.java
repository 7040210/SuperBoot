package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> 职务信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("职务请求实体信息")
public class ReqDuties extends BaseRequest {
    @NotBlank
    @ApiModelProperty("职务编码")
    private String dutiesCode;
    @NotBlank
    @ApiModelProperty("职务名称")
    private String dutiesName;
}
