package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.base.BaseRequest;
import org.superboot.entity.dto.RoleUserDTO;

import java.util.List;


/**
 * <b> 用户角色信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("用户角色请求实体信息")
public class ReqRoleUser extends BaseRequest {

    @ApiModelProperty("角色用户信息")
    List<RoleUserDTO> users;
}
