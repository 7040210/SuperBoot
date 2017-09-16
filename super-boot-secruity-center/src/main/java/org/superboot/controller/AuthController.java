package org.superboot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;
import org.superboot.entity.Token;
import org.superboot.service.AuthService;

/**
 * <b> 提供用户注册及访问授权功能 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 18:48
 * @Path org.superboot.controller.base.AuthController
 */
@Api(tags = "授权管理", description = "提供用户注册及访问授权功能")
@RequestMapping("/auth")
@RestController
public class AuthController {


    @Autowired
    private AuthService authService;

    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN")
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public BaseResponse createToken(
            @RequestBody LoginUser loginUser) throws SuperBootException {
        return authService.genToken(loginUser);
    }

    @ApiOperation(value = "刷新TOKEN", notes = "刷新TOKEN")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public BaseResponse refreshToken(@RequestBody Token token) throws SuperBootException {
        return authService.refresh(token);
    }

}