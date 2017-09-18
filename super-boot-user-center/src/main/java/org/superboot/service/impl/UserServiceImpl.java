package org.superboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.superboot.base.BaseResponse;
import org.superboot.base.BaseToken;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;
import org.superboot.entity.RegisterUser;
import org.superboot.entity.business.UcenterUser;
import org.superboot.entity.business.UcenterUserRole;
import org.superboot.pub.Pub_Tools;
import org.superboot.repository.sql.base.BaseApiRoleRepository;
import org.superboot.repository.sql.business.UcenterRoleRepository;
import org.superboot.repository.sql.business.UcenterUserRepository;
import org.superboot.repository.sql.business.UcenterUserRoleRepository;
import org.superboot.service.UserService;
import org.superboot.utils.DateUtils;
import org.superboot.utils.MD5Util;
import org.superboot.utils.RandomStrUtils;

/**
 * <b> 用户服务 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 16:09
 * @Path org.superboot.service.impl.UserServiceImpl
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UcenterUserRepository userRepository;
    @Autowired
    private UcenterUserRoleRepository sysUserRoleRepository;
    @Autowired
    private UcenterRoleRepository sysRoleRepository;
    @Autowired
    private BaseApiRoleRepository baseApiRoleRepository;

    @Override
    public BaseResponse register(RegisterUser regUser) {
        return saveUser(regUser, 0);
    }

    @Override
    public BaseResponse register_admin(RegisterUser regUser) {
        return saveUser(regUser, -1);
    }

    @Override
    public BaseResponse login(LoginUser loginUser) {
        String usercode = loginUser.getUsername();
        if (null == usercode) {
            throw new SuperBootException(SuperBootCode.USERNAME_NOT_FIND);
        }


        String rawPassword = loginUser.getPassword();
        if (null == rawPassword) {
            throw new SuperBootException(SuperBootCode.PASSWORD_NOT_FIND);
        }

        UcenterUser user = userRepository.findByUserCode(usercode);
        if (null == user) {
            throw new SuperBootException(SuperBootCode.ACCOUNT_NOT_FIND);
        }

        BaseToken token = new BaseToken();
        token.setUserid(user.getPkUser());
        token.setUsername(usercode);

        return new BaseResponse(token);
    }


    /**
     * 生成用户信息
     *
     * @param regUser   注册提交内容
     * @param user_type 用户类型 -1管理员 其他为普通用户
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public BaseResponse saveUser(RegisterUser regUser, int user_type)   {

        String usercode = regUser.getUsercode();
        if (null == usercode) {
            throw new SuperBootException(SuperBootCode.USERNAME_NOT_FIND);
        }


        String rawPassword = regUser.getPassword();
        if (null == rawPassword) {
            throw new SuperBootException(SuperBootCode.PASSWORD_NOT_FIND);
        }

        if (userRepository.findByUserCode(usercode) != null) {
            throw new SuperBootException(SuperBootCode.ADD_ERROR_EXISTS);
        }

        String random = RandomStrUtils.genRandom();

        //对Rsa加密信息进行解密
        //rawPassword = Pub_Tools.RSAdecrypt(RSAUtils.DEFAULT_PRIVATE_KEY, rawPassword);


        //构造默认用户
        UcenterUser SysUser = new UcenterUser();
        //进行密码加密
        SysUser.setUserPassword(genPassWord(rawPassword, random));
        SysUser.setLastPasswordResetDate(DateUtils.getTimestamp());
        SysUser.setUserCode(usercode);
        SysUser.setRandom(random);
        SysUser.setPkUser(Pub_Tools.genUUID());

        SysUser = userRepository.save(SysUser);


        //设置默认用户角色
        UcenterUserRole role = new UcenterUserRole();
        role.setPkUser(SysUser.getPkUser());
        role.setUserRoleId(Pub_Tools.genUUID());
        if (-1 == user_type) {
            role.setPkRole(sysRoleRepository.findByRoleCode("ROLE_ADMIN").getPkRole());
        } else {
            role.setPkRole(sysRoleRepository.findByRoleCode("ROLE_USER").getPkRole());
        }
        sysUserRoleRepository.save(role);


        BaseToken token = new BaseToken();
        token.setUserid(SysUser.getPkUser());
        token.setUsername(usercode);
        if (100 / 0 == 5) {
            return new BaseResponse(SuperBootCode.ADD_SUCCESS, token);
        }

        return new BaseResponse(SuperBootCode.ADD_SUCCESS, token);
    }


    /**
     * 生成密码
     *
     * @param passWord 明文密码
     * @param random   随机码
     * @return
     */
    public static String genPassWord(String passWord, String random) {
        String pdOne = MD5Util.MD5(passWord);
        return MD5Util.MD5(pdOne + random);
    }
}
