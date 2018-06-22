package org.superboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.entity.dto.UserMenuDTO;
import org.superboot.entity.request.*;
import org.superboot.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <b> 用户基础服务类  </b>
 * <p>
 * 功能描述:提供用户的基础服务，比如开放用户注册、用户登陆、修改密码
 * </p>
 */
@RequestMapping("/users")
@RestController
@AuthRoles(roles = {BaseConstants.ALL_USER_NAME})
@Api(tags = "用户中心用户基础服务", description = "提供用户的相关接口管理")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private Pub_RedisUtils redisUtils;

    @MethedNotValidateToken
    @ApiOperation(value = "注册普通用户", notes = "注册普通用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponse<BaseToken> addUser(@RequestBody @Validated RegisterUser regUser) throws BaseException {
        return userService.register(regUser);
    }


    @ApiOperation(value = "登陆", notes = "用户登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @MethedNotValidateToken
    public BaseResponse<BaseToken> login(
            @RequestBody @Validated LoginUser loginUser) throws BaseException {
        return userService.login(loginUser);
    }


    @ApiOperation(value = "授权登陆", notes = "第三方系统授权登陆，返回TOKEN信息")
    @RequestMapping(value = "/sso", method = RequestMethod.POST)
    @MethedNotValidateToken
    public BaseResponse<BaseToken> sso(
            @RequestBody @Validated ReqOtherLogin otherLogin) throws BaseException {
        return userService.sso(otherLogin);
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public BaseResponse password(@RequestBody @Validated ReqPassWord passWord, HttpServletRequest request) throws BaseException {
        BaseToken baseToken = redisUtils.getTokenByRequest(request);
        return userService.password(baseToken.getUserId(), passWord);
    }


    @ApiOperation(value = "获取用户菜单", notes = "获取用户菜单，如果出现菜单授权变更，需要重新登陆")
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public BaseResponse<List<UserMenuDTO>> getUserMenu(HttpServletRequest request) throws BaseException {
        BaseToken baseToken = redisUtils.getTokenByRequest(request);
        return new BaseResponse(redisUtils.getUserMenu(baseToken.getUserId()));
    }


    @ApiOperation(value = "变更用户基础信息", notes = "变更用户姓名、状态、失效日期，其他信息均不提供变更")
    @AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.GROUP_ADMIN_NAME})
    @RequestMapping(value = "/{pkUser}/base", method = RequestMethod.PUT)
    public BaseResponse setUserBase(@PathVariable("pkUser") long userId, @RequestBody @Validated ReqUserBase userBase) throws BaseException {
        return userService.setUserBase(userId, userBase);
    }

}