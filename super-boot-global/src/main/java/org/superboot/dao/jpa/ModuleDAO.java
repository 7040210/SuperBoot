package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootModule;

/**
 * <b> 模块信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
public interface ModuleDAO extends BaseJpaDAO<SuperbootModule> {

    /**
     * 获取模块信息
     *
     * @param moduleId 模块编码
     * @param dr       删除标识
     * @return
     */
    SuperbootModule findByModuleIdAndDr(String moduleId, int dr);

}

