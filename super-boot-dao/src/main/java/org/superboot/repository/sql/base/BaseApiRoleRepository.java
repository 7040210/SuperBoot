package org.superboot.repository.sql.base;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.superboot.entity.base.BaseApiRole;

import java.util.List;

/**
 * <b> 接口角色表 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 17:00
 * @Path org.superboot.repository.sql.base.BaseApiRoleRepository
 */
@CacheConfig(cacheNames = "apis")
public interface BaseApiRoleRepository  extends JpaRepository<BaseApiRole, Long> {

    @Cacheable(key="#p0")
    List<BaseApiRole> findByPkRole(long pk_role);

    @CachePut(key="#p0.pkRole")
    BaseApiRole save(BaseApiRole api);

}
