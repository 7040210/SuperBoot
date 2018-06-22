package org.superboot.service;

import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;

/**
 * <b> 资源权限信息服务类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
public interface PermissionsService {


    /**
     * 获取资源权限列表
     *
     * @return
     */
    BaseResponse getAll() throws BaseException;

    /**
     * 获取资源权限列表
     *
     * @param pageable 分页信息
     * @return
     */
    BaseResponse getPermissions(Pageable pageable) throws BaseException;

    /**
     * 获取资源详细信息
     *
     * @param permissionId 权限ID
     * @return
     * @throws BaseException
     */
    BaseResponse getPermission(long permissionId) throws BaseException;


    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @return
     * @throws BaseException
     */
    BaseResponse delPermission(long permissionId) throws BaseException;
}
