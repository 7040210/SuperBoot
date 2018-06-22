package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <b> 组织基础信息 </b>
 * <p>
 * 功能描述:获取组织基础信息
 * </p>
 *
 */
@Data
@ApiModel("组织响应实体基础信息")
public class ResGroup implements Serializable {

    @ApiModelProperty("组织主键")
    @NotNull
    private Long pkGroup;

    @ApiModelProperty("组织编码")
    @NotBlank
    private String groupCode;

    @ApiModelProperty("组织名称")
    @NotBlank
    private String groupName;

    @ApiModelProperty("组织类型（0为 自建、1为授权、2为其他）")
    @NotNull
    private Integer groupType;

    public ResGroup() {
        super();
    }
}
