package org.superboot.secruity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.superboot.entity.base.sys.SysUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <b> JWT验证用户信息工厂类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 18:40
 * @Path org.superboot.secruity.JwtUserFactory
 */
public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    /**
     * 创建JWT用户信息
     * @param user 用户信息
     * @param roles 用户角色列表
     * @return
     */
    public static JwtUser create(SysUser user, List<String> roles) {
        return new JwtUser(
                user.getPkUser(),
                user.getUserCode(),
                user.getUserPassword(),
                user.getUserEmail(),
                mapToGrantedAuthorities(roles),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}