package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootResource;

/**
 * <b> 接口信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
public interface ResourceDAO extends BaseJpaDAO<SuperbootResource> {


    /**
     * 根据url和方法名获取对象信息 必须全局唯一
     *
     * @param url        URL路径
     * @param methodName 方法名称
     * @param moduleId   模块ID
     * @return
     */
    SuperbootResource findByUrlAndMethodNameAndModuleId(String url, String methodName, String moduleId);

    /**
     * 根据微服务ID 类路径 类名称获取 对象信息 必须全局唯一
     *
     * @param moduleId   模块ID
     * @param methodPath 类名
     * @param methodName 方法名
     * @return
     */
    SuperbootResource findByModuleIdAndMethodPathAndMethodName(String moduleId, String methodPath, String methodName);

}
