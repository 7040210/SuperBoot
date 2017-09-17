package org.superboot.repository.sql.business;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.superboot.entity.business.UcenterUserRole;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:49
 * @Path org.superboot.repository.sql.business.UcenterUserRoleRepository
 */
@Service
@CacheConfig(cacheNames = "default")
public interface UcenterUserRoleRepository extends JpaRepository<UcenterUserRole, Long> {

    @Cacheable(key = "#p0")
    UcenterUserRole findByPkUser(String userid);


    @CachePut(key = "#p0.pkUser")
    UcenterUserRole save(UcenterUserRole userrole);
}
