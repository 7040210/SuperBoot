package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootDuties;
import org.superboot.entity.jpa.SuperbootDuties;

/**
 * <b> 职务信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "duties")
public interface DutiesDAO extends BaseJpaDAO<SuperbootDuties>, QuerydslBinderCustomizer<QSuperbootDuties> {

    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param duties
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootDuties duties) {
        bindings.bind(duties.dutiesCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(duties.dutiesName).first(StringExpression::containsIgnoreCase);
    }
}
