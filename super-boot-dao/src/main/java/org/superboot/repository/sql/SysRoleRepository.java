package org.superboot.repository.sql;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.superboot.entity.base.sys.SysRole;

import java.util.List;

/**
 * <b> 系统角色表DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 17:43
 * @Path org.superboot.repository.sql.SysRoleRepository
 */

@CacheConfig(cacheNames = "roles")
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {


    /**
     * 查询系统角色，使用角色编码作为KEY
     *
     * @param roleCode
     * @return
     */
    @Cacheable
    SysRole findByRoleCode(String roleCode);

    @Cacheable
    SysRole findOne(Long pk_role);

    @Cacheable
    List<SysRole> findAll();


    /**
     * 根据用户PK查询用户角色信息,为了防止角色发生变动造成新角色无法读取，可以不存储缓存
     * @param Pk_user
     * @return
     */
    @Query(value = "select * from sys_role r where r.dr=0 and r.pk_role in (select o.pk_role from sys_user_role o where o.dr=0 and o.pk_user = ?1 )" ,nativeQuery = true)
    List<SysRole> findSysRoleByPkUser(Long Pk_user);

}
