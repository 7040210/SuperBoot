package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootEmployees;
import org.superboot.entity.jpa.SuperbootEmployees;

/**
 * <b> 雇员信息 </b>
 * <p>
 * 功能描述:雇员基础信息表操作
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "employees")
public interface EmployeesDAO extends BaseJpaDAO<SuperbootEmployees>, QuerydslBinderCustomizer<QSuperbootEmployees> {
    /**
     * 根据员工主键和删除状态查找
     *
     * @param pkEmployees
     * @param dr
     * @return
     */
    SuperbootEmployees findByPkEmployeesAndDr(long pkEmployees, int dr);

    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param emp
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootEmployees emp) {
        bindings.bind(emp.employeesCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(emp.employeesName).first(StringExpression::containsIgnoreCase);
    }

    /**
     * 按员工编号和删除标识查找
     *
     * @param employeesCode 员工编号
     * @param dr            删除标识
     * @return
     */
    SuperbootEmployees findByEmployeesCodeAndDr(String employeesCode, int dr);
}
