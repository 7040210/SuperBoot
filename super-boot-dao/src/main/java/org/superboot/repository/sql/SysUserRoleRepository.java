package org.superboot.repository.sql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.superboot.entity.base.sys.SysRole;
import org.superboot.entity.base.sys.SysUserRole;

import java.util.List;

/**
 * <b> 用户角色表DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 17:41
 * @Path org.superboot.repository.sql.SysUserRoleRepository
 */
@CacheConfig(cacheNames = "default")
public interface SysUserRoleRepository extends JpaRepository<SysUserRole,Long>{


}
