package org.superboot.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.base.sys.SysRole;
import org.superboot.entity.base.sys.SysUser;
import org.superboot.pub.Pub_LocalTools;
import org.superboot.repository.sql.SysRoleRepository;
import org.superboot.repository.sql.SysUserRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <b> 根据用户账号查询用户信息及角色信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 17:14
 * @Path org.superboot.pub.JwtUserDetailsServiceImpl
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Resource
    Pub_LocalTools local;

    @Override
    public UserDetails loadUserByUsername(String usercode) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUserCode(usercode);
        if (user == null) {
            throw new UsernameNotFoundException(local.getMessage(SuperBootCode.TOKEN_INVALID.getMessage()));
        } else {
            List list = new ArrayList();
            List<SysRole> user_roles = sysRoleRepository.findSysRoleByPkUser(user.getPkUser());
            for (SysRole role : user_roles) {
                list.add(role.getRoleCode());
            }
            return JwtUserFactory.create(user, list);
        }
    }
}
