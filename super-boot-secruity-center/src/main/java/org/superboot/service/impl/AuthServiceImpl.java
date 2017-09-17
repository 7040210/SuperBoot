package org.superboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.superboot.base.*;
import org.superboot.entity.LoginUser;
import org.superboot.entity.Token;
import org.superboot.pub.Pub_JWTUtils;
import org.superboot.pub.Pub_Tools;
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

    @Autowired
    private Pub_JWTUtils jwtTokenUtil;

    @Autowired
    private Pub_Tools Pub_Tools;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BaseResponse genToken(LoginUser loginUser) {

        BaseToken bt = new BaseToken();
        BaseMessage message = new BaseMessage();
        message = restTemplate.postForObject(  "http://127.0.0.1:2222/users/login", loginUser, BaseMessage.class, message);
        if(SuperBootStatus.OK.getCode() ==message.getStatus()){
            if(SuperBootCode.OK.getCode() == message.getCode() ){
                JSONArray array = (JSONArray) JSON.toJSON(message.getData());
                JSONObject data = (JSONObject) array.get(0);
                bt.setUserid(Long.valueOf(data.get("userid").toString()));
                bt.setUsername(data.get("username").toString());
                HashMap map = new HashMap();
                map.put("token", jwtTokenUtil.generateToken(bt));
                return new BaseResponse(map);
            }
        }

        return new BaseResponse(SuperBootCode.ACCOUNT_DISABLED);
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
