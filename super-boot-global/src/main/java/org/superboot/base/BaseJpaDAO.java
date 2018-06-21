package org.superboot.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <b> JPA数据库操作基类 </b>
 * <p>
 * 功能描述:继承JpaRepository 实现基本的CRUD
 * 继承JpaSpecificationExecutor 实现复杂查询的支持
 * 继承QueryDslPredicateExecutor 实现对DSL语法支持
 * </p>
 */
@NoRepositoryBean
public interface BaseJpaDAO<T>
        extends
        JpaRepository<T, Long>,
        JpaSpecificationExecutor<T>,
        QueryDslPredicateExecutor<T> {

}