package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <b> 员工基本信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Data
@ApiModel("员工响应实体信息")
public class ResEmployees implements Serializable {

    @ApiModelProperty("员工主键")
    @NotNull
    private Long pkEmployees;

    @NotBlank
    @ApiModelProperty("员工编码")
    private String employeesCode;

    @NotBlank
    @ApiModelProperty("员工名称")
    private String employeesName;

    @ApiModelProperty("职务主键")
    private Long pkDuties;

    public ResEmployees() {
        super();
    }
}
