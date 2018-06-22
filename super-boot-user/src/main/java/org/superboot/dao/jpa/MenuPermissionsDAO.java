package org.superboot.dao.jpa;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootMenuPermissions;

/**
 * <b> 菜单授权DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "menupermissions")
public interface MenuPermissionsDAO extends BaseJpaDAO<SuperbootMenuPermissions> {

    /**
     * 根据菜单主键、权限主键获取菜单权限信息
     *
     * @param pkMenu        菜单主键
     * @param pkPermissions 权限主键
     * @param dr            删除标识
     * @return
     */
    @Cacheable(key = "#p0+#p1+#p2")
    SuperbootMenuPermissions findByPkMenuAndPkPermissionsAndDr(long pkMenu, long pkPermissions, int dr);
}
