package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 机构信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("机构响应信息")
public class ResOrg implements Serializable {

    @ApiModelProperty("机构主键")
    private Long pkOrg;

    @ApiModelProperty("组织主键")
    private Long pkGroup;


    @ApiModelProperty("机构编码")
    private String orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构类型 0:集团 1:公司 2:部门  数据传输无需加密")
    private Integer nodetype;

    @ApiModelProperty("上级机构  数据传输无需加密")
    private Long pkFOrg;

    @ApiModelProperty("机构级别  数据传输无需加密")
    private Integer orgLev;

    @ApiModelProperty("是否为末级机构  数据传输无需加密")
    private String isEnd;

    @ApiModelProperty("机构顺序")
    private String orderCode;

    public ResOrg() {
        super();
    }
}
