package org.superboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.base.LoginUser;
import org.superboot.entity.base.Token;
import org.superboot.entity.base.api.RegisterUser;
import org.superboot.entity.base.sys.SysUser;
import org.superboot.entity.base.sys.SysUserRole;
import org.superboot.repository.sql.SysRoleRepository;
import org.superboot.repository.sql.SysUserRepository;
import org.superboot.repository.sql.SysUserRoleRepository;
import org.superboot.secruity.JwtTokenUtil;
import org.superboot.service.AuthService;
import org.superboot.utils.DateUtils;

import java.util.HashMap;

/**
 * <b> 授权功能具体实现 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 18:41
 * @Path org.superboot.service.impl.AuthServiceImpl
 */
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private SysUserRepository userRepository;
    private SysUserRoleRepository sysUserRoleRepository;
    private SysRoleRepository sysRoleRepository;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            SysUserRepository userRepository, SysUserRoleRepository userRoleRepository, SysRoleRepository RoleRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.sysUserRoleRepository = userRoleRepository;
        this.sysRoleRepository = RoleRepository;
    }


    @Override
    public BaseResponse register_admin(RegisterUser regUser) {
        return saveUser(regUser, -1);
    }


    @Override
    public BaseResponse register(RegisterUser regUser) {
        return saveUser(regUser, 0);
    }


    @Override
    public BaseResponse login(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUser.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        HashMap map = new HashMap();
        map.put("token",tokenHead+" "+ token);

        return new BaseResponse(map);
    }

    @Override
    public BaseResponse refresh(Token oldToken) {
        String token = oldToken.getToken();
        if(null ==  token || token.length() <= tokenHead.length()){
            throw new SuperBootException(SuperBootCode.TOKEN_NOT_FIND);
        }

        String tokenStr = jwtTokenUtil.refreshToken(token);
        if(null == tokenStr){
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        HashMap map = new HashMap();
        map.put("token",tokenHead+" "+ tokenStr);

        return new BaseResponse(map);
    }


    /**
     * 生成用户信息
     *
     * @param regUser   注册提交内容
     * @param user_type 用户类型 -1管理员 其他为普通用户
     * @return
     */
    @Transactional
    public BaseResponse saveUser(RegisterUser regUser, int user_type) {

        String usercode = regUser.getUsercode();
        if(null == usercode){
            throw new SuperBootException(SuperBootCode.USERNAME_NOT_FIND);
        }



        String rawPassword = regUser.getPassword();
        if(null == rawPassword){
            throw new SuperBootException(SuperBootCode.PASSWORD_NOT_FIND);
        }

        if (userRepository.findByUserCode(usercode) != null) {
            throw  new SuperBootException(SuperBootCode.ADD_ERROR_EXISTS);
        }

        //对密码进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //构造默认用户
        SysUser SysUser = new SysUser();
        SysUser.setUserPassword(encoder.encode(rawPassword));
        SysUser.setLastPasswordResetDate(DateUtils.getTimestamp());
        SysUser.setUserCode(usercode);
        SysUser.setUserPhone(regUser.getPhone());
        SysUser.setUserEmail(regUser.getEmail());
        //默认为非认证用户
        SysUser.setUserAuth(0);
        SysUser = userRepository.save(SysUser);


        //设置默认用户角色
        SysUserRole role = new SysUserRole();
        role.setPkUser(SysUser.getPkUser());
        if (-1 == user_type) {
            role.setPkRole(sysRoleRepository.findByRoleCode("ROLE_ADMIN").getPkRole());
        } else {
            role.setPkRole(sysRoleRepository.findByRoleCode("ROLE_USER").getPkRole());
        }
        sysUserRoleRepository.save(role);

        HashMap map = new HashMap();
        map.put("id",SysUser.getPkUser());

        return new BaseResponse(SuperBootCode.ADD_SUCCESS,map);
    }
}
