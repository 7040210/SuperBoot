package org.superboot.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("登陆用户机构扩展信息")
public class OrgDTO {

    private String unitType;

    private String unitName;


    private Long pk;
}
