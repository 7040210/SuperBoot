package org.superboot.entity.request;

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

    public Token(String token) {
        this.token = token;
    }
}
