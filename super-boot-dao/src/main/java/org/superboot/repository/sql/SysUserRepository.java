package org.superboot.repository.sql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.superboot.entity.base.sys.SysUser;

/**
 * <b> 用户操作实体类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 16:32
 * @Path org.superboot.repository.sql.SysUserRepository
 */
@Service
@CacheConfig(cacheNames = "users")
public interface SysUserRepository extends JpaRepository<SysUser , Long>{

    /**
     * 根据用户账号查询用户信息 缓存使用用户的账号作为KEY
     * @param userCode 用户账号
     * @return
     */
    @Cacheable
    SysUser findByUserCode(String userCode);



}
