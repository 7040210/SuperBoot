package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b> 菜单信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("注册菜单请求实体信息")
public class ReqMenu extends BaseRequest {

    @ApiModelProperty("菜单编码")
    @NotBlank
    private String menuCode;

    @ApiModelProperty("菜单名称")
    @NotBlank
    private String menuName;

    @ApiModelProperty("菜单类型 0为后台系统菜单 1为前台业务菜单 数据传输无需加密")
    @NotNull
    private Integer menuType;

    @ApiModelProperty("上级菜单主键 数据传输无需加密")
    private Long pkFMenu;

    @ApiModelProperty("菜单访问路径")
    private String menuUrl;

    @ApiModelProperty("菜单排序号，使用三位一级")
    @NotBlank
    private String orderCode;

    @ApiModelProperty("菜单图表")
    private String menuIco;
}
