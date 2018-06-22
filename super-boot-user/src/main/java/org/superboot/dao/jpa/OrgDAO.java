package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootOrg;
import org.superboot.entity.jpa.SuperbootOrg;

/**
 * <b> 机构信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author zhangshuai
 * @date 2017/11/14
 * @time 10:41
 * @Path org.superboot.dao.jpa.ImpOrgDAO
 */
@Repository
@CacheConfig(cacheNames = "orgs")
public interface OrgDAO extends BaseJpaDAO<SuperbootOrg>, QuerydslBinderCustomizer<QSuperbootOrg> {

    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param org
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootOrg org) {
        bindings.bind(org.orgName).first(StringExpression::containsIgnoreCase);
    }

    /**
     * 根据编码查询机构信息
     *
     * @param orgCode 机构代码
     * @param dr      删除标志
     * @return
     */
    SuperbootOrg findByOrgCodeAndDr(String orgCode, int dr);

    /**
     * 根据编码和组织查询机构信息
     *
     * @param pkGroup 组织主键
     * @param orgCode 机构代码
     * @param dr      删除标志
     * @return
     */
    SuperbootOrg findByPkGroupAndOrgCodeAndDr(long pkGroup, String orgCode, int dr);

    /**
     * 根据主键查询机构信息
     *
     * @param pkOrg 机构主键
     * @param dr    删除标志
     * @return
     */
    SuperbootOrg findByPkOrgAndDr(long pkOrg, int dr);
}
