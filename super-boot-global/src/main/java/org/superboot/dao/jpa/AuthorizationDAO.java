package org.superboot.dao.jpa;

import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.SuperbootAuthorization;

/**
 * <b> 第三方授权信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
public interface AuthorizationDAO extends BaseJpaDAO<SuperbootAuthorization> {


    /**
     * 根据accessKey与secretKey查询授权信息
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    SuperbootAuthorization findByAccessKeyAndSecretKey(String accessKey, String secretKey);


}
