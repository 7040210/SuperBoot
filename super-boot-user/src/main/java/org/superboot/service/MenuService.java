package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqMenu;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResMenu;

import java.util.List;

/**
 * <b> 角色管理服务类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface MenuService {

    /**
     * 添加菜单
     *
     * @param menu 菜单信息
     * @return
     * @throws BaseException
     */
    BaseResponse addMenu(ReqMenu menu) throws BaseException;

    /**
     * 添加菜单
     *
     * @param menuId 菜单主键
     * @param menu   菜单信息
     * @return
     * @throws BaseException
     */
    BaseResponse setMenu(long menuId, ReqMenu menu) throws BaseException;

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     * @throws BaseException
     */
    BaseResponse delMenu(long menuId) throws BaseException;

    /**
     * 获取菜单信息
     *
     * @param menuId
     * @return
     * @throws BaseException
     */
    BaseResponse getMenu(long menuId) throws BaseException;

    /**
     * 查询菜单列表
     *
     * @param pkGroup   组织主键，系统管理传入-1
     * @param pageable  分页信息
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse getMenus(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException;


    /**
     * 查询菜单总数
     *
     * @param pkGroup   组织主键，系统管理传入-1
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse getCount(long pkGroup, Predicate predicate) throws BaseException;

    /**
     * 获取角色菜单
     *
     * @param pkRole 角色主键
     * @return
     * @throws BaseException
     */
    BaseResponse<List<ResMenu>> getMenusByRole(Long pkRole) throws BaseException;

    /**
     * 获取角色没有的菜单
     *
     * @param pkRole
     * @param pkGroup
     * @param pageable
     * @param predicate
     * @return
     * @throws BaseException
     */
    BaseResponse<List<ResMenu>> getRoleNoMenu(Long pkRole, Long pkGroup, Pageable pageable, Predicate predicate) throws BaseException;

    /**
     * 检查报表菜单是否存在
     *
     * @param pkTemplate
     * @return
     * @throws BaseException
     */
    BaseResponse<ResCount> cheReportMenu(Long pkTemplate) throws BaseException;
}
