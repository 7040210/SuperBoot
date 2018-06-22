package org.superboot.controller.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.service.ErrorLogService;
import org.superboot.service.LogService;

/**
 * <b> 后台管理首页面 </b>
 * <p>
 * 功能描述: 用于后台管理打开首页面的时候显示的内容，多数为图表信息
 * </p>
 *
 */
@RequestMapping("/sys")
@RestController
@Api(tags = "用户中心首页管理公共接口", description = "后台首页管理，提供一些图表类的接口")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.DEV_USER_NAME, BaseConstants.OPER_USER_NAME})
public class SysIndexController {


    @Autowired
    private ErrorLogService errorLogService;


    @Autowired
    private LogService logService;


    @ApiOperation(value = "获取今天错误日志分组信息", notes = "获取今天错误日志分组信息，系统管理员默认可以访问")
    @RequestMapping(value = "/todayErrorCount", method = RequestMethod.GET)
    public BaseResponse getTodayErrorCount() throws BaseException {
        return errorLogService.getErrorLogGroupByAppName();
    }


    @ApiOperation(value = "获取过去7天各模块请求信息", notes = "获取过去7天各模块请求信息，系统管理员默认可以访问")
    @RequestMapping(value = "/requestCountByWeek", method = RequestMethod.GET)
    public BaseResponse getRequestCountByWeek() throws BaseException {
        return logService.getRequestCountByWeek();
    }

}
