package org.superboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.common.utils.JWT_Utils;
import org.superboot.entity.request.LoginUser;
import org.superboot.entity.request.RegisterUser;
import org.superboot.entity.request.ReqOtherLogin;
import org.superboot.entity.request.Token;
import org.superboot.entity.response.ResToken;
import org.superboot.remote.UserRemote;
import org.superboot.service.PubService;
import org.superboot.utils.AESUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Service
public class PubServiceImpl extends BaseService implements PubService {

    @Autowired
    JWT_Utils jwtUtils;

    @Autowired
    UserRemote userRemote;


    @Autowired
    private Pub_RedisUtils redisUtils;


    /**
     * RSA解密
     *
     * @param text 密文
     * @return
     */
    private String rsaDecrypt(String text) throws Exception {
        //使用hutool进行RSA解密，这个的优势是免去使用第三方JAR包的依赖
        RSA rsa = new RSA(BaseConstants.DEFAULT_PKCS8_PRIVATE_KEY, BaseConstants.DEFAULT_PUBLIC_KEY);
        //URL转码
        return new String(rsa.decrypt(Base64.decode(text), KeyType.PrivateKey), CharsetUtil.UTF_8);
    }

    /**
     * 获取ASE密钥信息
     *
     * @return
     */
    private String getAesKey() {
        ServletRequestAttributes attributes = getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String secretKey = request.getHeader(BaseConstants.SECRET_KEY);
        if (StrUtil.isNotBlank(secretKey)) {
            //执行RSA解密
            try {
                return rsaDecrypt(secretKey);
            } catch (Exception e) {
                throw new BaseException(StatusCode.DECODE_FAIL);
            }
        }

        return null;
    }

    /**
     * 执行数据解密
     *
     * @param value 加密后内容
     * @return
     */
    public String aesDecrypt(String value) {
        String secretKey = getAesKey();
        if (null != secretKey) {
            //解密密文信息
            try {
                String deValue = AESUtil.aesDecrypt(value, secretKey);
                //临时过渡时期，前端出现的BUG因为部分数字未加密解密后为空串造成接口错误，后期前台改完后此处删除掉
                if (StrUtil.isBlank(deValue) && !value.endsWith("=")) {
                    return value;
                }
                return deValue;
            } catch (Exception e) {
                return value;
            }
        }

        return value;
    }


    /**
     * 设置用户相关信息
     *
     * @param baseToken 用户信息
     * @param token     token
     * @return
     */
    private ResToken genResToken(BaseToken baseToken, String token) {
        ResToken resToken = new ResToken();
        //设置TOKEN
        resToken.setToken(token);

        //用户账号
        resToken.setUserName(baseToken.getUserName());

        //设置用户主键
        resToken.setUserPk(baseToken.getUserId());
        //设置用户公司
        resToken.setGroupPk(baseToken.getPkGroup());

        //设置关联用户
        resToken.setPkEmp(baseToken.getPkEmp());

        //设置所属机构
        resToken.setPkOrg(baseToken.getPkOrg());

        //设置最后登陆日期
        resToken.setLoginDate(baseToken.getLoginDate());

        //设置最后登陆时间
        resToken.setLoginTime(baseToken.getLoginTime());

        //账号停用时间
        resToken.setEndTime(baseToken.getEndTime());

        //用户名称
        resToken.setFullName(baseToken.getFullName());


        //员工姓名
        resToken.setEmpName(baseToken.getEmpName());

        //机构名称
        resToken.setOrgName(baseToken.getOrgName());

        //职务名称
        resToken.setDutiesName(baseToken.getDutiesName());


        //设置户类型

        if (-1 == baseToken.getPkGroup()) {
            //如果包含系统管理员角色则为系统管理员
            if (-1 != baseToken.getUserRole().indexOf(BaseConstants.SYS_ADMIN_NAME)) {
                resToken.setUserType(-1);
            }
            //其他情况为系统级操作用户比如运维 开发人员
            else {
                resToken.setUserType(1);
            }
        } else {
            //如果包含组织管理员角色则为组织管理员
            if (-1 != baseToken.getUserRole().indexOf(BaseConstants.GROUP_ADMIN_NAME)) {
                resToken.setUserType(0);
            }
            //如果包含开放注册角色则为公开注册用户
            else if (-1 != baseToken.getUserRole().indexOf(BaseConstants.PUB_USER_NAME)) {
                resToken.setUserType(3);
            }
            //其他为组织授权用户
            else {
                resToken.setUserType(2);
            }
        }

        return resToken;
    }


    @Override
    public BaseResponse login(LoginUser loginUser) {

        if (isAes()) {
            //执行数据解密
            loginUser.setPlatform(aesDecrypt(loginUser.getPlatform()));
            loginUser.setVersion(aesDecrypt(loginUser.getVersion()));
            loginUser.setUserCode(aesDecrypt(loginUser.getUserCode()));
            loginUser.setUserPassword(aesDecrypt(loginUser.getUserPassword()));
        }


        //调用用户中心服务获取登陆信息
        BaseResponse response = userRemote.login(loginUser);
        if (BaseStatus.OK.getCode() == response.getStatus()) {
            //判断数据状态
            if (StatusCode.OK.getCode() == response.getCode()) {
                JSONObject data = (JSONObject) JSON.toJSON(response.getData());
                BaseToken bt = JSON.toJavaObject(data, BaseToken.class);

                //判断白名单，用户是否已经生成过TOKEN了，如果生成过则不再进行登陆操作
                String token = getRedisUtils().getTokenAllow(bt.getUserId(), loginUser.getPlatform(), loginUser.getVersion());
                if (null == token) {
                    //生成新的TOKEN
                    token = jwtUtils.generateToken(bt);
                    //登陆后将TOKEN信息放入缓存
                    getRedisUtils().setTokenInfo(token, bt);
                    getRedisUtils().setTokenAllow(bt.getUserId(), loginUser.getPlatform(), loginUser.getVersion(), token);
                } else {
                    //判断TOKEN是否已经锁定了，如果锁定了则需要重新生成新的TOKEN给客户端
                    ValueOperations<String, HashMap> operations = getRedisUtils().getRedisTemplate().opsForValue();
                    boolean exists = getRedisUtils().getRedisTemplate().hasKey(token);
                    if (exists) {
                        HashMap tokenItem = operations.get(token);
                        //如果TOKEN已经锁定，则重新生成TOKEN信息
                        if (Pub_RedisUtils.TOKEN_STATUS_LOCKED.equals(tokenItem.get(Pub_RedisUtils.TOKEN_STATUS))) {
                            //生成新的TOKEN
                            token = jwtUtils.generateToken(bt);
                            //登陆后将TOKEN信息放入缓存
                            getRedisUtils().setTokenInfo(token, bt);
                            getRedisUtils().setTokenAllow(bt.getUserId(), loginUser.getPlatform(), loginUser.getVersion(), token);
                        }
                    }
                }

                return new BaseResponse(genResToken(bt, token));
            }

        }
        return response;

    }

    @Override
    public BaseResponse sso(ReqOtherLogin otherLogin) {

        if (isAes()) {
            //执行数据解密
            otherLogin.setPlatform(aesDecrypt(otherLogin.getPlatform()));
            otherLogin.setVersion(aesDecrypt(otherLogin.getVersion()));
            otherLogin.setUserKey(aesDecrypt(otherLogin.getUserKey()));
        }


        //调用用户中心服务获取登陆信息
        BaseResponse response = userRemote.sso(otherLogin);
        if (BaseStatus.OK.getCode() == response.getStatus()) {
            //判断数据状态
            if (StatusCode.OK.getCode() == response.getCode()) {
                JSONObject data = (JSONObject) JSON.toJSON(response.getData());
                BaseToken bt = JSON.toJavaObject(data, BaseToken.class);

                //判断白名单，用户是否已经生成过TOKEN了，如果生成过则不再进行登陆操作
                String token = getRedisUtils().getTokenAllow(bt.getUserId(), otherLogin.getPlatform(), otherLogin.getVersion());
                if (null == token) {
                    //生成新的TOKEN
                    token = jwtUtils.generateToken(bt);
                    //登陆后将TOKEN信息放入缓存
                    getRedisUtils().setTokenInfo(token, bt);
                    getRedisUtils().setTokenAllow(bt.getUserId(), otherLogin.getPlatform(), otherLogin.getVersion(), token);
                } else {
                    //判断TOKEN是否已经锁定了，如果锁定了则需要重新生成新的TOKEN给客户端
                    ValueOperations<String, HashMap> operations = getRedisUtils().getRedisTemplate().opsForValue();
                    boolean exists = getRedisUtils().getRedisTemplate().hasKey(token);
                    if (exists) {
                        HashMap tokenItem = operations.get(token);
                        //如果TOKEN已经锁定，则重新生成TOKEN信息
                        if (Pub_RedisUtils.TOKEN_STATUS_LOCKED.equals(tokenItem.get(Pub_RedisUtils.TOKEN_STATUS))) {
                            //生成新的TOKEN
                            token = jwtUtils.generateToken(bt);
                            //登陆后将TOKEN信息放入缓存
                            getRedisUtils().setTokenInfo(token, bt);
                            getRedisUtils().setTokenAllow(bt.getUserId(), otherLogin.getPlatform(), otherLogin.getVersion(), token);
                        }
                    }
                }

                return new BaseResponse(genResToken(bt, token));
            }

        }
        return response;
    }

    @Override
    public BaseResponse tokenInfo() {

        ServletRequestAttributes attributes = getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(BaseConstants.TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            throw new BaseException(StatusCode.TOKEN_INVALID);
        }

        BaseToken baseToken = getBaseToken();

        return new BaseResponse(genResToken(baseToken, token));
    }


    /**
     * 数据是否进行加密，部分接口不允许不加密请求
     *
     * @return
     */
    private boolean isAes() {
        if (StrUtil.isBlank(getAesKey())) {
            return false;
        }
        return true;
    }

    @Override
    public BaseResponse logout(String token) {

        //获取用户信息
        BaseToken bt = getRedisUtils().getTokenInfo(token);
        //清理TOKEN白名单信息
        getRedisUtils().cleanTokenAllow(bt.getUserId(), token);
        //从Redis中清理TOKEN信息
        getRedisUtils().getRedisTemplate().delete(token);

        return new BaseResponse(StatusCode.OPERATION_SUCCESS);
    }

    @Override
    public BaseResponse register(RegisterUser registerUser) {

        if (!isAes()) {
            //执行数据解密
            registerUser.setPlatform(aesDecrypt(registerUser.getPlatform()));
            registerUser.setVersion(aesDecrypt(registerUser.getVersion()));
            registerUser.setUserCode(aesDecrypt(registerUser.getUserCode()));
            registerUser.setUserPassword(aesDecrypt(registerUser.getUserPassword()));
            registerUser.setUserEmail(aesDecrypt(registerUser.getUserEmail()));
            registerUser.setUserPhone(aesDecrypt(registerUser.getUserPhone()));
            registerUser.setUserName(aesDecrypt(registerUser.getUserName()));
            registerUser.setEndTime(aesDecrypt(registerUser.getEndTime()));
        }


        BaseResponse response = userRemote.addUser(registerUser);
        if (BaseStatus.OK.getCode() == response.getStatus()) {
            //判断数据状态
            if (StatusCode.ADD_SUCCESS.getCode() == response.getCode()) {
                JSONObject data = (JSONObject) JSON.toJSON(response.getData());
                BaseToken bt = JSON.toJavaObject(data, BaseToken.class);

                //判断白名单，用户是否已经生成过TOKEN了，如果生成过则不再进行登陆操作
                String token = getRedisUtils().getTokenAllow(bt.getUserId(), registerUser.getPlatform(), registerUser.getVersion());
                if (null == token) {
                    //生成新的TOKEN
                    token = jwtUtils.generateToken(bt);
                    //登陆后将TOKEN信息放入缓存
                    getRedisUtils().setTokenInfo(token, bt);
                    getRedisUtils().setTokenAllow(bt.getUserId(), registerUser.getPlatform(), registerUser.getVersion(), token);
                }

                return new BaseResponse(StatusCode.ADD_SUCCESS, genResToken(bt, token));
            }

        }
        return response;
    }


    @Override
    public BaseResponse refresh(String token) {
        if (null == token) {
            throw new BaseException(StatusCode.TOKEN_NOT_FIND);
        }
        //刷新生成新的TOKEN
        String tokenStr = jwtUtils.refreshToken(token);
        if (null == tokenStr) {
            throw new BaseException(StatusCode.TOKEN_INVALID);
        }
        //根据老的TOKEN获取用户信息
        BaseToken baseToken = getRedisUtils().getTokenInfo(token);

        //判断白名单，用户是否已经生成过TOKEN了，如果生成过则不再进行登陆操作
        HashMap<String, String> tokenItem = getRedisUtils().getTokenAllowItem(baseToken.getUserId(), token);
        if (null == token) {
            throw new BaseException(StatusCode.TOKEN_INVALID);
        } else {
            String version = tokenItem.get(BaseConstants.TOKEN_VERSION);
            String platform = tokenItem.get(BaseConstants.TOKEN_PLATFORM);
            //清理TOKEN
            getRedisUtils().cleanTokenAllow(baseToken.getUserId(), token);
            //从Redis中清理TOKEN信息
            getRedisUtils().getRedisTemplate().delete(token);
            //更新缓存信息
            getRedisUtils().setTokenInfo(tokenStr, baseToken);
            //更新新的TOKEN到白名单
            getRedisUtils().setTokenAllow(baseToken.getUserId(), platform, version, tokenStr);

        }
        return new BaseResponse(genResToken(baseToken, tokenStr));
    }

    @Override
    public BaseResponse locked(Token oldToken) {

        getRedisUtils().tokenLocked(oldToken.getToken(), true);
        return new BaseResponse(StatusCode.LOCKED_SUCCESS);
    }

    @Override
    public BaseResponse unLocked(Token oldToken) {
        getRedisUtils().tokenLocked(oldToken.getToken(), false);
        return new BaseResponse(StatusCode.UNLOCKED_SUCCESS);
    }

    @Override
    public BaseResponse checkUserIsManager(String token) {
        BaseToken baseToken = getRedisUtils().getTokenInfo(token);
        if (null != baseToken) {
            //判断用户的角色是否为空
            if (!StringUtils.isBlank(baseToken.getUserRole())) {
                //获取系统有操作权限的角色
                String[] roles = BaseConstants.ADMIN_ROLE_TYPE.split(",");
                for (String role : roles) {
                    if (-1 != role.indexOf(baseToken.getUserRole())) {
                        return new BaseResponse(StatusCode.AUTHORIZATION_OPERATION);
                    }
                }
            }
        }

        return new BaseResponse(StatusCode.UNAUTHORIZED_OPERATION);
    }

}
