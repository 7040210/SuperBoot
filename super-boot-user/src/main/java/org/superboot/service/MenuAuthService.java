package org.superboot.service;

import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqMenuAuth;

/**
 * <b> 菜单授权 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface MenuAuthService {

    /**
     * 菜单授权
     *
     * @param menuAuth 授权信息
     * @return
     * @throws BaseException
     */
    BaseResponse addMenuAuth(ReqMenuAuth menuAuth) throws BaseException;

    /**
     * 菜单取消授权
     *
     * @param menuPermissionsId 授权id
     * @return
     * @throws BaseException
     */
    BaseResponse delMenuAuth(long menuPermissionsId) throws BaseException;


    /**
     * 获取菜单授权列表
     *
     * @param menuId 菜单主键
     * @return
     * @throws BaseException
     */
    BaseResponse getMenuAuthList(long menuId) throws BaseException;
}
