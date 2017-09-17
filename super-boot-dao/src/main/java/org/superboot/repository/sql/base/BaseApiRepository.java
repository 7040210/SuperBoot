package org.superboot.repository.sql.base;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
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
@CacheConfig(cacheNames = "apis")
public interface BaseApiRepository  extends JpaRepository<BaseApi, Long> {



    @Cacheable(key="#p0+#p1")
    List<BaseApi> findByUrlAndMethodName(String url, String methodName);

    @CachePut(key="#p0.url+#p0.methodName")
    BaseApi save(BaseApi api);
}
