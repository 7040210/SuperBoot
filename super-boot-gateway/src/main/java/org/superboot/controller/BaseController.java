package org.superboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.*;
import org.superboot.entity.request.LoginUser;
import org.superboot.entity.request.RegisterUser;
import org.superboot.entity.request.ReqOtherLogin;
import org.superboot.entity.request.Token;
import org.superboot.entity.response.ResDate;
import org.superboot.entity.response.ResToken;
import org.superboot.service.PubService;
import org.superboot.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <b> 开放服务接口 </b>
 * <p>
 * 功能描述:提供注册、登陆、获取公钥等公开免认证的服务接口
 * </p>
 *
 */
@RequestMapping("/PubApi")
@RestController
@AuthRoles(roles = {BaseConstants.ALL_USER_NAME})
@Api(tags = "网关中心公共服务接口", description = "提供网关的公共服务及通用组件功能")
public class BaseController {


    @Autowired
    private PubService pubService;


    @ApiOperation(value = "用户登陆", notes = "用户登陆，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @MethedNotValidateToken
    public BaseResponse<ResToken> login(
            @RequestBody @Validated LoginUser loginUser) throws BaseException {
        return pubService.login(loginUser);
    }


    @ApiOperation(value = "授权登陆", notes = "第三方系统授权登陆，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用")
    @RequestMapping(value = "/sso", method = RequestMethod.POST)
    @MethedNotValidateToken
    public BaseResponse<ResToken> sso(
            @RequestBody @Validated ReqOtherLogin otherLogin) throws BaseException {
        return pubService.sso(otherLogin);
    }


    @ApiOperation(value = "获取TOKEN的详细信息", notes = "第三方系统授权登陆后，页面集成用到的token关联用户信息")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public BaseResponse<ResToken> tokenInfo() throws BaseException {
        return pubService.tokenInfo();
    }


    @ApiOperation(value = "用户注册", notes = "用户注册，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @MethedNotValidateToken
    public BaseResponse<ResToken> register(
            @RequestBody @Validated RegisterUser registerUser) throws BaseException {
        return pubService.register(registerUser);
    }

    @ApiOperation(value = "刷新TOKEN", notes = "刷新TOKEN,一般用于TOKEN锁定或者过期的时候使用")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public BaseResponse<ResToken> refreshToken(HttpServletRequest request) throws BaseException {
        String token = request.getHeader(BaseConstants.TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            throw new BaseException(StatusCode.TOKEN_NOT_FIND);
        }
        return pubService.refresh(token);
    }

    @ApiOperation(value = "用户退出", notes = "用户退出,一般用于网站登陆时候使用")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public BaseResponse logout(HttpServletRequest request) throws BaseException {
        String token = request.getHeader(BaseConstants.TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            throw new BaseException(StatusCode.TOKEN_NOT_FIND);
        }
        return pubService.logout(token);
    }

    @ApiOperation(value = "锁定TOKEN", notes = "锁定TOKEN,需要指定角色才有此权限")
    @RequestMapping(value = "/locked", method = RequestMethod.POST)
    public BaseResponse lockedToken(@RequestBody @Validated Token token) throws BaseException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        BaseResponse op = pubService.checkUserIsManager("" + request.getHeader(BaseConstants.TOKEN_KEY));
        if (StatusCode.AUTHORIZATION_OPERATION.getCode() == op.getCode()) {
            return pubService.locked(token);
        } else {
            return op;
        }
    }

    @ApiOperation(value = "解锁TOKEN", notes = "解锁TOKEN,需要指定角色才有此权限")
    @RequestMapping(value = "/unlocked", method = RequestMethod.POST)
    public BaseResponse unlockedToken(@RequestBody @Validated Token token) throws BaseException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        BaseResponse op = pubService.checkUserIsManager("" + request.getHeader(BaseConstants.TOKEN_KEY));
        if (StatusCode.AUTHORIZATION_OPERATION.getCode() == op.getCode()) {
            return pubService.unLocked(token);
        } else {
            return op;
        }
    }

    @ApiOperation(value = "获取RSA公钥", notes = "获取RSA公钥，用于客户端加密AES密钥使用")
    @RequestMapping(value = "/key", method = RequestMethod.GET)
    @MethedNotValidateToken
    public BaseResponse getPubKey() throws BaseException {
        return new BaseResponse(BaseConstants.DEFAULT_PUBLIC_KEY);
    }


    @MethedNotValidateToken
    @ApiOperation(value = "格式化日期用于手机应用使用", notes = "格式化日期用于手机应用使用，显示为几分钟前、几小时前等")
    @RequestMapping(value = "/formatShowDate/{date}/{time}", method = RequestMethod.GET)
    @AuthRoles(roles = {BaseConstants.ALL_USER_NAME})
    public BaseResponse formatShowDate(@PathVariable("date") String date, @PathVariable("time") String time) throws BaseException {

        return new BaseResponse(DateUtils.formatShow(date + " " + time));
    }

    @MethedNotValidateToken
    @ApiOperation(value = "获取服务器时间信息", notes = "获取服务器的时间信息")
    @RequestMapping(value = "/sysDate", method = RequestMethod.GET)
    @AuthRoles(roles = {BaseConstants.ALL_USER_NAME})
    public BaseResponse<ResDate> getSysDate() throws BaseException {

        ResDate resDate = new ResDate();

        resDate.setCurrTime(DateUtils.getCurrentTime());
        resDate.setCurrDate(DateUtils.getCurrentDate());
        resDate.setCurrMonth(resDate.getCurrDate().substring(0, 7));
        resDate.setCurrYear(DateUtils.getCurrentYear());


        resDate.setCurrWeek(DateUtils.getWeekByDate(resDate.getCurrDate()));
        resDate.setFirstDayOfWeek(DateUtils.getFirstDayOfWeek(resDate.getCurrDate()));
        resDate.setLastDayOfWeek(DateUtils.getLastDayOfWeek(resDate.getCurrDate()));


        resDate.setCurrQuarter(DateUtils.getQuarterOfYear(resDate.getCurrDate()));
        resDate.setFirstDayOfQuarter(DateUtils.getFirstDayOfQuarter(resDate.getCurrDate()));
        resDate.setLastDayOfQuarter(DateUtils.getLastDayOfQuarter(resDate.getCurrDate()));

        resDate.setCurrCnXq(DateUtils.getCnWeekDay(resDate.getCurrDate()));
        resDate.setCurrNumXq(DateUtils.getNumByCnWeek(resDate.getCurrCnXq()));

        resDate.setBeginDate(DateUtils.getCurrentDate().substring(0, 7) + "-01");
        resDate.setEndDate(DateUtils.getEndDateOfMonth(resDate.getCurrDate()));

        resDate.setLeapYear(DateUtils.isLeapYear(resDate.getCurrDate()));

        return new BaseResponse<>(resDate);

    }
}
