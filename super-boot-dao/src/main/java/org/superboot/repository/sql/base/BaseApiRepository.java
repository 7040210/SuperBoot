package org.superboot.repository.sql.base;

import org.springframework.stereotype.Service;
import org.superboot.base.BaseDAO;
import org.superboot.entity.base.BaseApi;

import java.util.List;

/**
 * <b> 接口信息表 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:59
 * @Path org.superboot.repository.sql.base.BaseApiRepository
 */
@Service
public interface BaseApiRepository extends BaseDAO<BaseApi> {


    Object findByUrlAndMethodName(String url, String methodName);

    @Override
    BaseApi save(BaseApi api);
}
