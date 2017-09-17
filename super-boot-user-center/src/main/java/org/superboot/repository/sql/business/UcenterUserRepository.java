package org.superboot.repository.sql.business;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.superboot.entity.business.UcenterUser;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:49
 * @Path org.superboot.repository.sql.business.UcenterUserRepository
 */
@Service
@CacheConfig(cacheNames = "users")
public interface UcenterUserRepository extends JpaRepository<UcenterUser, Long> {

    /**
     * 根据用户账号查询用户信息 缓存使用用户的账号作为KEY
     * @param userCode 用户账号
     * @return
     */
    @Cacheable(key = "#p0")
    UcenterUser findByUserCode(String userCode);


    @CachePut(key = "#p0.userCode")
    UcenterUser save(UcenterUser user);

}
