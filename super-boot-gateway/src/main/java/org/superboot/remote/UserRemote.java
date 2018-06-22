package org.superboot.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.LoginUser;
import org.superboot.entity.request.RegisterUser;
import org.superboot.entity.request.ReqOtherLogin;

/**
 * <b> 远程调用用户中心接口 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@FeignClient(name = "user")
public interface UserRemote {


    /**
     * 登陆
     *
     * @param loginUser 登陆用户信息
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    BaseResponse login(@RequestBody @Validated LoginUser loginUser) throws BaseException;


    /**
     * 第三方授权登陆
     *
     * @param otherLogin 授权信息
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/users/sso", method = RequestMethod.POST)
    BaseResponse sso(@RequestBody @Validated ReqOtherLogin otherLogin) throws BaseException;

    /**
     * 注册账户
     *
     * @param regUser 注册用户信息
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    BaseResponse addUser(@RequestBody @Validated RegisterUser regUser) throws BaseException;
}
