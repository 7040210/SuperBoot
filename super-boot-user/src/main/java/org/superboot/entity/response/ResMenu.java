package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("菜单响应实体信息")
public class ResMenu implements Serializable {

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单类型 0为后台系统菜单 1为前台业务菜单 数据传输无需加密")
    @NotNull
    private Integer menuType;


    @ApiModelProperty("菜单主键 数据传输无需加密")
    private Long pkMenu;

    @ApiModelProperty("上级菜单主键 数据传输无需加密")
    private Long pkFMenu;

    @ApiModelProperty("菜单访问路径")
    private String menuUrl;

    @ApiModelProperty("菜单排序号，使用三位一级")
    private String orderCode;

    @ApiModelProperty("菜单图表")
    private String menuIco;

    @ApiModelProperty("是否末级菜单 N为否 Y为是,非末级菜单不能绑定业务功能")
    private String isEnd;

    public ResMenu(){
        super();
    }
}
