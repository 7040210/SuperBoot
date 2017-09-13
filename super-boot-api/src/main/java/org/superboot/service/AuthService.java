package org.superboot.service;

import org.superboot.base.BaseResponse;
import org.superboot.entity.base.LoginUser;
import org.superboot.entity.base.Token;
import org.superboot.entity.base.api.RegisterUser;

/**
 * <b> 授权及用户注册服务 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 18:40
 * @Path org.superboot.service.AuthService
 */
public interface AuthService {
    /**
     * 注册普通用户
     *
     * @param regUser
     * @return
     */
    BaseResponse register(RegisterUser regUser);

    /**
     * 注册管理员
     *
     * @param regUser
     * @return
     */
    BaseResponse register_admin(RegisterUser regUser);

    /**
     * 获取TOKEN
     *
     * @param loginUser 登录用户
     * @return
     */
    BaseResponse login(LoginUser loginUser);

    /**
     * 刷新TOKEN
     *
     * @param oldToken
     * @return
     */
    BaseResponse refresh(Token oldToken);
}
