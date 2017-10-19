package org.superboot.pub.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.ServerInfo;
import org.superboot.utils.DateUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <b> 获取服务器信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/10/17
 * @time
 * @Path org.superboot.pub.controller.ServerController
 */
@Api(tags = "获取服务器信息", description = "获取服务器信息")
@RequestMapping("/server")
@RestController
public class ServerController {

    @Autowired
    private Environment env;

    /**
     * 获取服务器相关信息用于后期服务巡检使用
     * @return
     */
    @ApiOperation(value = "获取服务器信息", notes = "获取服务器相关信息用于后期服务巡检使用")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public BaseResponse getInfo(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new SuperBootException(SuperBootCode.EXCEPTION,e);
        }
        ServerInfo info = new ServerInfo();
        info.setUrl(host);
        info.setPort(env.getProperty("server.port"));
        info.setSerName(env.getProperty("spring.application.name"));
        info.setTime(DateUtils.getCurrentDateTime());
        return new BaseResponse(info);
    }
}
