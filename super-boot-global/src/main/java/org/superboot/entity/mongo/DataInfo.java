package org.superboot.entity.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.superboot.base.BaseMongoEntity;

/**
 * <b> 记录消息传输的整个调用链信息 </b>
 * <p>
 * 功能描述:存储具体调用日志信息，索引建立原则为先筛选，后排序，最后放多余条件
 * </p>
 */
@CompoundIndexes({
        @CompoundIndex(name = "dataId", def = "{'dataId':1}"),
        @CompoundIndex(name = "localAddr", def = "{'localAddr':1}"),
        @CompoundIndex(name = "userName", def = "{'userName':1}"),
        @CompoundIndex(name = "userId", def = "{'userId':1}"),
        @CompoundIndex(name = "classMethod", def = "{'classMethod':1}"),
        @CompoundIndex(name = "execDate", def = "{'execDate':-1}"),
        @CompoundIndex(name = "execTime", def = "{'execTime':-1}"),
        @CompoundIndex(name = "interfaceName", def = "{'dataId':1}"),

        @CompoundIndex(name = "appSer_execTime", def = "{'appSer':1,'execTime':-1}"),
        @CompoundIndex(name = "appName_execTime", def = "{'appName':1,'execTime':-1}"),
        @CompoundIndex(name = "appName_execDate", def = "{'appName':1,'execDate':-1}"),
        @CompoundIndex(name = "classMethod_execDate", def = "{'classMethod':1,'execDate':-1}"),
        @CompoundIndex(name = "reqUrl_execDate", def = "{'classMethod':1,'execDate':-1}"),
        @CompoundIndex(name = "interfaceName_execDate", def = "{'interfaceName':1,'execDate':-1}")
})
@Document
@Data
@ApiModel("业务日志信息")
@Entity
public class DataInfo extends BaseMongoEntity {


    @ApiModelProperty(value = "消息ID")
    private String dataId;

    @ApiModelProperty(value = "执行服务信息")
    private String serinfo;

    @ApiModelProperty(value = "headers信息")
    private String headers;

    @ApiModelProperty(value = "Cookie信息")
    private String cookie;


    @ApiModelProperty(value = "请求人机器名")
    private String localName;

    @ApiModelProperty(value = "请求人IP地址")
    private String localAddr;

    @ApiModelProperty(value = "模块地址")
    private String serUrl;


    @ApiModelProperty(value = "模块端口")
    private String serPort;


    @ApiModelProperty(value = "应用名称")
    private String appName;


    @ApiModelProperty(value = "模块名称")
    private String appSer;


    @ApiModelProperty(value = "数据中心ID")
    private long dataCenterId;


    @ApiModelProperty(value = "工作平台ID")
    private long workerId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "调用类")
    private String classMethod;

    @ApiModelProperty(value = "请求地址")
    private String reqUrl;

    @ApiModelProperty(value = "接口名称")
    private String interfaceName;

    @ApiModelProperty(value = "请求参数")
    private String parameters;

    @ApiModelProperty(value = "提交参数")
    private String args;

    @ApiModelProperty(value = "执行结果")
    private String returnMsq;

    @ApiModelProperty(value = "耗时")
    private long execCount;

    @ApiModelProperty(value = "执行日期")
    private String execDate;

    @ApiModelProperty(value = "执行时间")
    private String execTime;

}
