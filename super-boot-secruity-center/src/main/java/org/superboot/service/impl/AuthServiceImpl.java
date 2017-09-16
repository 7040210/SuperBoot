package org.superboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superboot.base.BaseResponse;
import org.superboot.base.BaseToken;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;
import org.superboot.entity.Token;
import org.superboot.pub.Pub_JWTUtils;
import org.superboot.service.AuthService;

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

    private Pub_JWTUtils jwtTokenUtil;

    @Autowired
    public AuthServiceImpl(
            Pub_JWTUtils jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @Override
    public BaseResponse genToken(LoginUser loginUser) {

        BaseToken bt = new BaseToken();
        bt.setUsername(loginUser.getUsername());
        bt.setUserid(123L);
        HashMap map = new HashMap();
        map.put("token", jwtTokenUtil.generateToken(bt));

        return new BaseResponse(map);
    }

    @Override
    public BaseResponse refresh(Token oldToken) {
        String token = oldToken.getToken();
        if (null == token) {
            throw new SuperBootException(SuperBootCode.TOKEN_NOT_FIND);
        }

        String tokenStr = jwtTokenUtil.refreshToken(token);
        if (null == tokenStr) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        HashMap map = new HashMap();
        map.put("token", tokenStr);

        return new BaseResponse(map);
    }

}
