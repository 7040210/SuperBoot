package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootPermissions;

/**
 * <b> 授权信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
public interface PermissionsDAO extends BaseJpaDAO<SuperbootPermissions> {

    /**
     * 根据编码、名称、删除标识获取权限资源信息
     *
     * @param code 编码
     * @param name 名称
     * @param dr   删除标识
     * @return
     */
    SuperbootPermissions findByPermissionsCodeAndPermissionsNameAndDr(String code, String name, int dr);


    /**
     * 根据编码、删除标识获取权限资源信息
     *
     * @param code 编码
     * @param dr   删除标识
     * @return
     */
    SuperbootPermissions findByPermissionsCodeAndDr(String code, int dr);
}
