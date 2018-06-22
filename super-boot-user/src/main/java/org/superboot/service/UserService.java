package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.*;

/**
 * <b> 用户管理服务 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface UserService {

    /**
     * 注册普通用户
     *
     * @param regUser
     * @return
     */
    BaseResponse register(RegisterUser regUser) throws BaseException;

    /**
     * 注册管理员
     *
     * @param regUser
     * @return
     */
    BaseResponse registerAdmin(RegisterUser regUser) throws BaseException;


    /**
     * 注册组织管理员
     *
     * @param regUser
     * @param groupId
     * @return
     */
    BaseResponse registerGroupAdmin(RegisterUser regUser, long groupId) throws BaseException;


    /**
     * 添加指定角色用户
     *
     * @param regUser 用户信息
     * @param pkRole  角色主键
     * @return
     * @throws BaseException
     */
    BaseResponse registerRoleUser(RegisterUser regUser, long pkRole) throws BaseException;


    /**
     * 注册组织人员
     *
     * @param regUser 用户信息
     * @param groupId 组织主键
     * @param pkRole  角色主键
     * @return
     * @throws BaseException
     */
    BaseResponse registerGroupUser(RegisterUser regUser, long groupId, long pkRole) throws BaseException;


    /**
     * 用户登陆
     *
     * @param loginUser 登录用户
     * @return
     */
    BaseResponse login(LoginUser loginUser) throws BaseException;


    /**
     * 第三方授权获取TOKEN
     *
     * @param otherLogin 登陆用户信息
     * @return
     */
    BaseResponse sso(ReqOtherLogin otherLogin);

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param passWord 修改密码传入信息
     * @return
     * @throws BaseException
     */
    BaseResponse password(long userId, ReqPassWord passWord) throws BaseException;



    /**
     * 变更用户姓名及失效日期
     *
     * @param userId   用户主键
     * @param userBase 调整内容
     * @return
     * @throws BaseException
     */
    BaseResponse setUserBase(long userId, ReqUserBase userBase) throws BaseException;



    /**
     * 获取用户列表
     *
     * @param pageable 分页信息
     * @param user     用户过滤条件
     * @param role     角色过滤条件
     * @param group    组织过滤条件
     * @return
     * @throws BaseException
     * @Param encryption  是否需要脱敏 true为脱敏 false为不脱敏
     */
    BaseResponse getUserList(Pageable pageable, boolean encryption, Predicate user, Predicate role, Predicate group) throws BaseException;

    /**
     * 获取用户总数
     *
     * @param user  用户过滤条件
     * @param role  角色过滤条件
     * @param group 组织过滤条件
     * @return
     * @throws BaseException
     */
    BaseResponse getUserCount(Predicate user, Predicate role, Predicate group) throws BaseException;

    /**
     * 删除用户
     *
     * @param userId 用户主键
     * @return
     * @throws BaseException
     */
    BaseResponse delUser(long userId) throws BaseException;

}
