package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.base.BaseRequest;
import org.superboot.entity.dto.MenuAuthDTO;

import java.util.List;

/**
 * <b> 菜单授权信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("菜单授权请求实体信息")
public class ReqMenuAuth extends BaseRequest {

    @ApiModelProperty("菜单权限对照")
    List<MenuAuthDTO> menuAuths;
}
