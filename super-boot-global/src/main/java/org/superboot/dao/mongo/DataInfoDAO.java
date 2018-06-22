package org.superboot.dao.mongo;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseMongoDAO;
import org.superboot.entity.mongo.DataInfo;

import java.util.List;

/**
 * <b> 数据请求日志信息DAO </b>
 * <p>
 * 功能描述:存储系统请求的LOG日志信息
 * </p>
 */
@Repository
public interface DataInfoDAO extends BaseMongoDAO<DataInfo> {

    /**
     * 根据消息ID查询业务流转的所有信息
     *
     * @param dataid
     * @return
     */
    List<DataInfo> findByDataId(String dataid);
}
