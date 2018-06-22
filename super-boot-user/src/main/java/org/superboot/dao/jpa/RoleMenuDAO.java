package org.superboot.dao.jpa;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootRoleMenu;

/**
 * <b> 角色菜单授权DAO </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "rolemenu")
public interface RoleMenuDAO extends BaseJpaDAO<SuperbootRoleMenu> {

    /**
     * 根据菜单主键与角色主键判断是否已经授权
     *
     * @param pkMenu 菜单主键
     * @param pkRole 角色主键
     * @param dr     删除标识
     * @return
     */
    SuperbootRoleMenu findByPkMenuAndPkRoleAndDr(long pkMenu, long pkRole, int dr);


}
