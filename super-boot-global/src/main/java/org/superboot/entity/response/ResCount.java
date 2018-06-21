package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b> 查询记录数 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Data
@ApiModel("查询记录数")
public class ResCount implements Serializable {

    @ApiModelProperty("记录数")
    private int count;

    public ResCount(long count) {
        super();
        this.count = Long.valueOf(count).intValue();
    }
}
