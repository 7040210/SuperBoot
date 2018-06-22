package org.superboot.dao.jpa;

import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootRole;
import org.superboot.entity.jpa.QSuperbootGroup;
import org.superboot.entity.jpa.QSuperbootRole;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

/**
 * <b> 角色操作DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "roles")
public interface RoleDAO extends BaseJpaDAO<SuperbootRole>,QuerydslBinderCustomizer<QSuperbootRole> {

    /**
     * 增加对查询条件的模糊搜索支持
     * @param bindings
     * @param role
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootRole role) {
        bindings.bind(role.roleCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(role.roleName).first(StringExpression::containsIgnoreCase);
    }

    /**
     * 查询系统角色，使用角色编码作为KEY
     *
     * @param roleCode
     * @return
     */
    @Cacheable(key = "#p0+#p1")
    SuperbootRole findByRoleCodeAndDr(String roleCode, int dr);

    @Cacheable(key = "#p0+#p1+#p2")
    SuperbootRole findByRoleCodeAndPkGroupAndDr(String roleCode, long pkGroup, int dr);


}
