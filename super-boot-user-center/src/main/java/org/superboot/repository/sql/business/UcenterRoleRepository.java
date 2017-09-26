package org.superboot.repository.sql.business;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.superboot.base.BaseDAO;
import org.superboot.entity.business.UcenterRole;

import java.util.List;

/**
 * <b> 角色表DAO </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:49
 * @Path org.superboot.repository.sql.business.UcenterRoleRepository
 */
@Service
@CacheConfig(cacheNames = "roles")
public interface UcenterRoleRepository extends BaseDAO<UcenterRole> {


    /**
     * 查询系统角色，使用角色编码作为KEY
     *
     * @param roleCode
     * @return
     */
    @Cacheable(key = "#p0")
    UcenterRole findByRoleCode(String roleCode);

    @CachePut(key = "#p0.roleCode")
    UcenterRole save(UcenterRole role);


    /**
     * 根据用户PK查询用户角色信息,为了防止角色发生变动造成新角色无法读取，可以不存储缓存
     *
     * @param Pk_user
     * @return
     */

    @Query(value = "select * from sys_role r where r.dr=0 and r.pk_role in (select o.pk_role from sys_user_role o where o.dr=0 and o.pk_user = ?1 )", nativeQuery = true)
    List<UcenterRole> findSysRoleByPkUser(Long Pk_user);
}
