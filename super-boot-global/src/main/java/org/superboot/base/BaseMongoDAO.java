package org.superboot.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <b> MongoDB数据库操作基类 </b>
 * <p>
 * 功能描述:继承MongoRepository 实现基本的CRUD
 * 继承QueryDslPredicateExecutor 实现对DSL语法支持
 * </p>
 */
@NoRepositoryBean
public interface BaseMongoDAO<T> extends MongoRepository<T, String>, QueryDslPredicateExecutor<T> {
}
