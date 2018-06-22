package org.superboot.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author zhangshuai
 * @date 2018/4/18
 * @time 15:37
 * @Path cn.phxg.entity.response.ResDate
 */
@Data
@ApiModel("服务器时间信息")
public class ResDate implements Serializable {


    @ApiModelProperty("当前时间")
    private String currTime;

    @ApiModelProperty("当前日期")
    private String currDate;

    @ApiModelProperty("当前月份")
    private String currMonth;

    @ApiModelProperty("当前年份")
    private String currYear;

    @ApiModelProperty("当前周")
    private int currWeek;

    @ApiModelProperty("当前季度")
    private int currQuarter;


    @ApiModelProperty("当前周的第一天")
    private String firstDayOfWeek;

    @ApiModelProperty("当前周的最后一天")
    private String lastDayOfWeek;



    @ApiModelProperty("当前季度的第一天")
    private String firstDayOfQuarter;

    @ApiModelProperty("当前季度的最后一天")
    private String lastDayOfQuarter;




    @ApiModelProperty("当前数字星期")
    private int currNumXq;

    @ApiModelProperty("当前中文星期")
    private String currCnXq;

    @ApiModelProperty("当月起始日期")
    private String beginDate;

    @ApiModelProperty("当月截止日期")
    private String endDate;

    @ApiModelProperty("是否瑞年")
    private boolean leapYear;

}
