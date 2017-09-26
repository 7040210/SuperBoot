package org.superboot.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * <b> 该接口继承自JpaRepository<T, Long>,如需自定义CRUD函数，可自己在继承该接口的子接口内实现 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/26
 * @time 14:42
 * @Path org.superboot.base.BaseDAO
 */
public interface BaseDAO<T>
        extends
        JpaRepository<T, Long>,
        JpaSpecificationExecutor<T>,
        Serializable
{

}