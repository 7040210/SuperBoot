package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqRole;
import org.superboot.entity.request.ReqRoleMenu;
import org.superboot.entity.request.ReqRoleUser;

/**
 * <b> 提供基于角色的维护管理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface RoleService {

    /**
     * 添加角色
     *
     * @param groupId 组织主键
     * @param role    角色信息
     * @return
     */
    BaseResponse addRole(long groupId, ReqRole role) throws BaseException;


    /**
     * 角色菜单授权
     *
     * @param roleMenuList 角色菜单信息
     * @return
     * @throws BaseException
     */
    BaseResponse addRoleMenu(ReqRoleMenu roleMenuList) throws BaseException;

    /**
     * 角色用户授权
     *
     * @param roleUser 角色用户信息
     * @return
     * @throws BaseException
     */
    BaseResponse addRoleUser(ReqRoleUser roleUser) throws BaseException;


    /**
     * 获取角色信息
     *
     * @param pkGroup   组织主键，系统角色传入-1
     * @param pageable  分页信息
     * @param predicate 过滤条件
     * @return
     * @throws BaseException
     */
    BaseResponse getRoles(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException;


    /**
     * 获取角色总数
     *
     * @param pkGroup   组织主键，系统角色传入-1
     * @param predicate 过滤条件
     * @return
     * @throws BaseException
     */
    BaseResponse getCount(long pkGroup, Predicate predicate) throws BaseException;


    /**
     * 获取全部角色
     *
     * @param pkGroup   组织主键，系统角色传入-1
     * @param predicate 过滤条件
     * @return
     * @throws BaseException
     */
    BaseResponse getRoleAll(long pkGroup, Predicate predicate) throws BaseException;

    /**
     * 删除角色
     *
     * @param roleId 角色主键
     * @return
     * @throws BaseException
     */
    BaseResponse delRole(long roleId) throws BaseException;


    /**
     * 修改角色
     *
     * @param roleId 角色主键
     * @param role   角色信息
     * @return
     * @throws BaseException
     */
    BaseResponse setRole(Long roleId, ReqRole role) throws BaseException;

}
