package org.superboot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.BaseRequest;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootException;
import org.superboot.entity.base.LoginUser;
import org.superboot.entity.base.Token;
import org.superboot.entity.base.api.RegisterUser;
import org.superboot.service.AuthService;

import javax.servlet.http.HttpServletRequest;

/**
 * <b> 提供用户注册及访问授权功能 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 18:48
 * @Path org.superboot.controller.AuthController
 */
@Api(tags = "授权管理", description = "提供用户注册及访问授权功能")
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN")
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public BaseResponse createToken(
            @RequestBody LoginUser loginUser)throws SuperBootException {
        return authService.login(loginUser);
    }

    @ApiOperation(value = "刷新TOKEN", notes = "刷新TOKEN")
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.POST)
    public BaseResponse refreshToken(@RequestBody Token token) throws SuperBootException{
        return authService.refresh(token);
    }

    @ApiOperation(value = "注册普通用户", notes = "注册普通用户")
    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public BaseResponse register(@RequestBody RegisterUser regUser)throws SuperBootException {
        return authService.register(regUser);
    }


}