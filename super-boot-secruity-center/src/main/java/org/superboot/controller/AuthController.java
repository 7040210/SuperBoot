package org.superboot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.BaseResponse;
import org.superboot.base.NotValidateToken;
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
@NotValidateToken
public class AuthController {


    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    private AuthService authService;


    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN")
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public BaseResponse createToken(
            @RequestBody @Validated LoginUser loginUser) throws SuperBootException {
        return authService.genToken(loginUser);
    }

    @ApiOperation(value = "刷新TOKEN", notes = "刷新TOKEN")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public BaseResponse refreshToken(@RequestBody @Validated Token token) throws SuperBootException {
        return authService.refresh(token);
    }

}