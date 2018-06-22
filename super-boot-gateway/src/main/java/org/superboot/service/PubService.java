package org.superboot.service;

import org.superboot.base.BaseResponse;
import org.superboot.entity.request.LoginUser;
import org.superboot.entity.request.RegisterUser;
import org.superboot.entity.request.ReqOtherLogin;
import org.superboot.entity.request.Token;

/**
 * <b> Token服务接口 </b>
 * <p>
 * 功能描述:提供基础TOKEN信息的接口
 * </p>
 */
public interface PubService {


    /**
     * 获取TOKEN
     *
     * @param loginUser 登录用户
     * @return
     */
    BaseResponse login(LoginUser loginUser);


    /**
     * 第三方授权获取TOKEN
     *
     * @param otherLogin 登陆用户信息
     * @return
     */
    BaseResponse sso(ReqOtherLogin otherLogin);


    /**
     * 获取token的详细信息
     *
     * @return
     */
    BaseResponse tokenInfo();


    /**
     * 退出
     *
     * @param token Token信息
     * @return
     */
    BaseResponse logout(String token);

    /**
     * 获取TOKEN
     *
     * @param registerUser 注册用户
     * @return
     */
    BaseResponse register(RegisterUser registerUser);

    /**
     * 刷新TOKEN
     *
     * @param token Token信息
     * @return
     */
    BaseResponse refresh(String token);


    /**
     * 锁定TOKEN
     *
     * @param oldToken Token信息
     * @return
     */
    BaseResponse locked(Token oldToken);


    /**
     * 解锁TOKEN
     *
     * @param oldToken Token信息
     * @return
     */
    BaseResponse unLocked(Token oldToken);

    /**
     * 校验当前登陆用户是否有权限执行用户锁定解锁功能
     *
     * @param token
     * @return
     */
    BaseResponse checkUserIsManager(String token);


}
