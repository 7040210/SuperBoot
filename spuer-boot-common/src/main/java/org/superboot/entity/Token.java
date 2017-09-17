package org.superboot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.superboot.base.BaseRequest;

/**
 * <b> Token信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/12
 * @time 13:30
 * @Path org.superboot.entity.Token
 */
@Data
@ApiModel("TOKEN信息")
public class Token extends BaseRequest {

    @ApiModelProperty(value = "Token")
    @NotBlank
    private String token;

    public Token() {
        super();
    }

    public Token(String token ) {
        this.token = token;
    }
}
