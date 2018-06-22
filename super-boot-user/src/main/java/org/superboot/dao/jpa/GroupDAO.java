package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootGroup;
import org.superboot.entity.jpa.SuperbootGroup;

/**
 * <b> 组织信息DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Repository
@CacheConfig(cacheNames = "groups")
public interface GroupDAO extends BaseJpaDAO<SuperbootGroup>, QuerydslBinderCustomizer<QSuperbootGroup> {

    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param group
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootGroup group) {
        bindings.bind(group.groupCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(group.groupName).first(StringExpression::containsIgnoreCase);
    }

    @Cacheable(key = "#p0+#p1")
    SuperbootGroup findByGroupCodeAndDr(String groupCode, int dr);

    /**
     * 根据主键和删除标识查找
     *
     * @param pkGroup
     * @param dr
     * @return
     */
    SuperbootGroup findByPkGroupAndDr(Long pkGroup, int dr);


}
