package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootOrg;

/**
 * <b> 机构信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
public interface OrgDAO extends BaseJpaDAO<SuperbootOrg> {
    /**
     * 根据编码查询机构信息
     *
     * @param orgCode 机构代码
     * @param dr      删除标志
     * @return
     */
    SuperbootOrg findByOrgCodeAndDr(String orgCode, int dr);
}

