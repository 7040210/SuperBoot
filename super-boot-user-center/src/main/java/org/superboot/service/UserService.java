package org.superboot.service;

import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;
import org.superboot.entity.RegisterUser;

/**
 * <b> 用户服务 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:09
 * @Path org.superboot.service.UserService
 */
public interface UserService {

    /**
     * 注册普通用户
     *
     * @param regUser
     * @return
     */
    BaseResponse register(RegisterUser regUser) throws SuperBootException;

    /**
     * 注册管理员
     *
     * @param regUser
     * @return
     */
    BaseResponse register_admin(RegisterUser regUser) throws SuperBootException;

    /**
     * 获取TOKEN
     *
     * @param loginUser 登录用户
     * @return
     */
    BaseResponse login(LoginUser loginUser);

}
