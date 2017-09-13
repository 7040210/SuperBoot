package org.superboot.repository.sql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.superboot.entity.base.sys.SysApi;

import java.util.List;

/**
 * <b> 系统接口资源DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 11:15
 * @Path org.superboot.repository.sql.SysApiRepository
 */
@CacheConfig(cacheNames = "configs")
public interface SysApiRepository extends JpaRepository<SysApi, Long> {


    @Cacheable
    List<SysApi> findByUrl(String url);

    @Cacheable
    List<SysApi> findByUrlAndMethodName(String url ,String methodName);
}
