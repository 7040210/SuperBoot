package org.superboot.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * <b> 雇员信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("员工请求实体信息")
public class ReqEmployees extends BaseRequest {

    @NotBlank
    @ApiModelProperty("员工编号")
    private String employeesCode;

    @NotBlank
    @ApiModelProperty("员工名字")
    private String employeesName;

    @NotNull
    @ApiModelProperty("机构主键")
    private Long pkOrganization;

    @NotNull
    @ApiModelProperty("组织主键")
    private Long pkGroup;

    @NotNull
    @ApiModelProperty("职务主键主键")
    private Long pkDuties;

}
