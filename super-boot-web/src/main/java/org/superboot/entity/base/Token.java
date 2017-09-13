package org.superboot.entity.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.superboot.base.BaseRequest;

import java.io.Serializable;

/**
 * <b> Token信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/12
 * @time 13:30
 * @Path org.superboot.entity.base.Token
 */
@Data
@ApiModel("TOKEN信息")
public class Token extends BaseRequest {

    @ApiModelProperty(value = "Token")
    private String token;

    public Token() {
        super();
    }

    public Token(String token ) {
        this.token = token;
    }
}
