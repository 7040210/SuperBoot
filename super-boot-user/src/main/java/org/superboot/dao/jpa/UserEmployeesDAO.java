package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootUserEmployees;

/**
 * <b> SuperbootUserEmployeesDAO </b>
 * <p>
 * 功能描述:用户员工关系服务
 * </p>
 */
@Repository
public interface UserEmployeesDAO extends BaseJpaDAO<SuperbootUserEmployees> {
    /**
     * 根据关系主键和删除状态查找
     *
     * @param pkUserEmployees
     * @param dr
     * @return
     */
    SuperbootUserEmployees findByPkUserEmployeesAndDr(long pkUserEmployees, int dr);

    /**
     * 根据员工主键和删除状态查找
     *
     * @param pkEmployees
     * @param dr
     * @return
     */
    SuperbootUserEmployees findByPkEmployeesAndDr(long pkEmployees, int dr);

    /**
     * 根据用户主键和删除状态查找
     *
     * @param pkUser
     * @param dr
     * @return
     */
    SuperbootUserEmployees findByPkUserAndDr(long pkUser, int dr);
}
