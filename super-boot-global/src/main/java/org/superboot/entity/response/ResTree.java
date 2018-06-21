package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("树形结构")
public class ResTree implements Serializable {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单code")
    private String menuCode;

    @ApiModelProperty("组织主键")
    private long pkGroup;

    @ApiModelProperty("子菜单")
    private List<ResTree> children;
}
