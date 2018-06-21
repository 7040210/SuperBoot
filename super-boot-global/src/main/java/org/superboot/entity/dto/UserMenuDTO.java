package org.superboot.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 用户菜单信息 </b>
 * <p>
 * 功能描述:用于存储用户的菜单信息
 * </p>
 */
@Data
@ApiModel("用户菜单信息")
public class UserMenuDTO implements Serializable {


    @ApiModelProperty("菜单编码")
    private String menuCode;
    @ApiModelProperty("菜单名称")
    private String menuName;
    @ApiModelProperty("菜单类型")
    private Integer menuType;
    @ApiModelProperty("内部序号")
    private String orderCode;
    @ApiModelProperty("菜单地址")
    private String menuUrl;
    @ApiModelProperty("菜单图表")
    private String menuIco;

    @ApiModelProperty("菜单级别")
    private Integer menuLev;
    @ApiModelProperty("是否末级菜单，只有末级菜单才是具体节点")
    private String isEnd;
    @ApiModelProperty("菜单主键")
    private Long pkMenu;
    @ApiModelProperty("上级菜单主键")
    private Long pkFMenu;


    public UserMenuDTO() {
        super();
    }
}
