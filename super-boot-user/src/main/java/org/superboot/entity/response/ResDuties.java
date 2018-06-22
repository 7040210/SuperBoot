package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 职务基础信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("职务响应实体基础信息")
public class ResDuties implements Serializable {

    @ApiModelProperty("职务编码")
    private String dutiesCode;


    @ApiModelProperty("职务名称")
    private String dutiesName;

    @ApiModelProperty("职务主键")
    private Long pkDuties;
}
