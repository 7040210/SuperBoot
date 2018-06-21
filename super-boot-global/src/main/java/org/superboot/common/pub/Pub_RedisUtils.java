package org.superboot.common.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseToken;
import org.superboot.base.StatusCode;
import org.superboot.entity.dto.UserMenuDTO;
import org.superboot.entity.dto.UserResourceDTO;
import org.superboot.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <b> 全局Redis工具类 </b>
 * <p>
 * 功能描述: 提供基于Redis的常用操作
 * </p>
 */
@Component
public class Pub_RedisUtils {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Pub_Tools pubTools;

    /**
     * Token的状态，后期如果需要踢出用户则直接将此状态改为 Y
     */
    public static final String TOKEN_STATUS = "TokenStatus";

    /**
     * 锁定TOKEN
     */
    public static final String TOKEN_STATUS_LOCKED = "Y";

    /**
     * TOKEN未锁定
     */
    public static final String TOKEN_STATUS_UNLOCKED = "N";

    /**
     * Token内存储的详细信息
     */
    public static final String TOKEN_ITEM = "TokenItem";

    /**
     * TOKEN数据过期时间 默认15天如果15天内用户没有进行过登陆会销毁TOKEN缓存信息
     */
    public static final int TOKEN_EXPIRED_DAY = 15;

    /**
     * 用户权限信息
     */
    public static final String USER_RESOURCE = "UserResource";

    /**
     * 用户菜单信息
     */
    public static final String USER_MENU = "UserMenu";


    /**
     * 获取Redis操作实例
     *
     * @return
     */
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }


    /**
     * 存储第三方请求消息ID与Token对照关系 默认5分钟有效
     *
     * @param messageId 消息ID
     * @param oToken    第三方授权TOKEN
     * @return
     */
    public void setOtherMessId(String messageId, String oToken) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(messageId, oToken, 5, TimeUnit.MINUTES);
    }

    /**
     * 根据第三方TOKEN信息获取公司主键
     *
     * @param messageId 第三方消息ID
     * @return
     */
    public Long getPkGroupByOtherMessageId(String messageId) {
        if (StringUtils.isNotBlank(messageId)) {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            boolean exists = redisTemplate.hasKey(messageId);
            if (exists) {
                String token = operations.get(messageId);

                return getPkGroupByOtherToken(token);
            }
        }

        return null;
    }


    /**
     * 设置第三方请求TOKEN与公司对照关系，默认有效期5分组
     *
     * @param oToken  TOKEN信息
     * @param pkGroup 公司主键
     */
    public void setOtherToken(String oToken, Long pkGroup) {
        ValueOperations<String, Long> operations = redisTemplate.opsForValue();
        operations.set(oToken, pkGroup, 5, TimeUnit.MINUTES);
    }


    /**
     * 根据第三方TOKEN信息获取公司主键
     *
     * @param oToken 授权TOKEN
     * @return
     */
    public Long getPkGroupByOtherToken(String oToken) {
        if (StringUtils.isNotBlank(oToken)) {
            ValueOperations<String, Long> operations = redisTemplate.opsForValue();
            boolean exists = redisTemplate.hasKey(oToken);
            if (exists) {
                return operations.get(oToken);
            } else {
                pubTools.checkOtherToken(oToken);
                return operations.get(oToken);
            }
        }

        return null;
    }

    /**
     * 设置会话的用户信息，默认保持时间为5分钟，如果后期涉及到长时间的操作，再调整超时时间
     *
     * @param messageId 会话消息ID
     * @param token     用户信息
     * @return
     */
    public boolean setSessionInfo(String messageId, BaseToken token) {
        //构造信息
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        boolean exists = redisTemplate.hasKey(messageId);
        if (exists) {
            return true;
        } else {
            operations.set(messageId, pubTools.objectToJson(token), 5, TimeUnit.MINUTES);
            return true;
        }

    }

    /**
     * 根据会话消息ID获取用户信息
     *
     * @param messageId 会话消息ID
     * @return
     */
    public BaseToken getSessionInfo(String messageId) {
        if (StringUtils.isNotBlank(messageId)) {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            boolean exists = redisTemplate.hasKey(messageId);
            if (exists) {
                return pubTools.jsonToObject(operations.get(messageId), BaseToken.class);
            }
        }
        return null;
    }

    /**
     * 设置登陆用户信息
     *
     * @param token     登陆TOKEN
     * @param baseToken 用户身份信息
     * @return
     */
    public BaseToken setTokenInfo(String token, BaseToken baseToken) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        //如果存在则先删除
        boolean exists = redisTemplate.hasKey(token);
        if (exists) {
            //更新用户最后登陆信息
            HashMap tokenItem = operations.get(token);
            tokenItem.put(TOKEN_ITEM, pubTools.objectToJson(baseToken));
            operations.set(token, tokenItem, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        } else {
            //将用户信息存储到Redis中
            HashMap tokenItem = new HashMap(2);
            tokenItem.put(TOKEN_STATUS, TOKEN_STATUS_UNLOCKED);
            tokenItem.put(TOKEN_ITEM, pubTools.objectToJson(baseToken));
            operations.set(token, tokenItem, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        }


        return baseToken;
    }

    /**
     * 根据TOKEN获取用户信息，每次调用都会刷新有效期
     *
     * @param token token
     * @return
     */
    public BaseToken getTokenInfo(String token) {
        if (StringUtils.isNotBlank(token)) {
            ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
            boolean exists = redisTemplate.hasKey(token);
            if (exists) {
                //更新用户最后登陆信息
                HashMap tokenItem = operations.get(token);
                //获取具体内容
                BaseToken baseToken = null;
                if (tokenItem.get(Pub_RedisUtils.TOKEN_ITEM) instanceof BaseToken) {
                    baseToken = (BaseToken) tokenItem.get(Pub_RedisUtils.TOKEN_ITEM);
                } else {
                    baseToken = pubTools.jsonToObject(tokenItem.get(Pub_RedisUtils.TOKEN_ITEM) + "", BaseToken.class);
                }

                if (null != tokenItem) {
                    //如果TOKEN已经锁定，则提示用户需要重新登陆
                    if (TOKEN_STATUS_LOCKED.equals(tokenItem.get(TOKEN_STATUS))) {
                        baseToken.setErrCode(StatusCode.TOKEN_LOCKED);
                    } else {
                        baseToken.setLoginTime(DateUtils.getCurrentTime());
                        baseToken.setLoginDate(DateUtils.getCurrentDate());
                        tokenItem.put(TOKEN_ITEM, pubTools.objectToJson(baseToken));
                        //每次用户登陆都刷新TOKEN的有效期
                        operations.set(token, tokenItem, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
                    }
                    return baseToken;
                }

            }
        }

        return null;
    }


    /**
     * 设置TOKEN白名单,KEY 为
     *
     * @param pkUser   用户主键
     * @param platform 来源平台
     * @param version  来源版本
     * @param token    TOKEN信息
     */
    public void setTokenAllow(long pkUser, String platform, String version, String token) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            //添加新的TOKEN信息到缓存中
            if (!tokenMap.containsKey(token)) {
                //TOKEN详细信息
                HashMap<String, String> itemMap = new HashMap(2);
                itemMap.put(BaseConstants.TOKEN_VERSION, version);
                itemMap.put(BaseConstants.TOKEN_PLATFORM, platform);
                tokenMap.put(token, itemMap);
            }
            //添加新的版本与平台信息到缓存中
            if (!tokenMap.containsKey(platform + "-" + version)) {
                tokenMap.put(platform + "-" + version, token);
            }

            //清理历史
            redisTemplate.delete(BaseConstants.USER_TOKEN + "-" + pkUser);
            //重新设置以便刷新最后失效时间
            operations.set(BaseConstants.USER_TOKEN + "-" + pkUser, tokenMap, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        } else {
            //TOKEN详细信息
            HashMap<String, String> itemMap = new HashMap(2);
            itemMap.put(BaseConstants.TOKEN_VERSION, version);
            itemMap.put(BaseConstants.TOKEN_PLATFORM, platform);
            HashMap<String, Object> tokenMap = new HashMap<>(1);
            tokenMap.put(token, itemMap);
            tokenMap.put(platform + "-" + version, token);

            //重新设置以便刷新最后失效时间
            operations.set(BaseConstants.USER_TOKEN + "-" + pkUser, tokenMap, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        }
    }

    /**
     * 根据来源平台与版本获取TOKEN
     *
     * @param pkUser   用户主键
     * @param platform 来源平台
     * @param version  来源版本
     * @return
     */
    public String getTokenAllow(long pkUser, String platform, String version) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            //循环处理TOKEN白名单里失效的TOKEN
            for (String key : tokenMap.keySet()) {
                //判断对象是TOKEN
                if (key.startsWith("Bearer")) {
                    //如果TOKEN已经不存在了
                    if (!redisTemplate.hasKey(key)) {
                        //清理失效TOKEN信息
                        cleanTokenAllow(pkUser, key);
                    }
                }
            }
            if (tokenMap.containsKey(platform + "-" + version)) {
                return "" + tokenMap.get(platform + "-" + version);
            }
        }
        return null;
    }

    /**
     * 根据TOKEN获取来源平台与版本
     *
     * @param pkUser 用户主键
     * @param token  TOKEN
     * @return
     */
    public HashMap<String, String> getTokenAllowItem(long pkUser, String token) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            if (tokenMap.containsKey(token)) {
                return (HashMap<String, String>) tokenMap.get(token);
            }
        }
        return null;
    }


    /**
     * 清理全部TOKEN信息（多用于修改密码后）
     *
     * @param pkUser 用户主键
     */
    public void cleanAllToken(long pkUser) {
        //清理白名单与TOKEN信息
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            //循环处理TOKEN白名单里失效的TOKEN
            for (String key : tokenMap.keySet()) {
                //判断对象是TOKEN
                if (key.startsWith("Bearer")) {
                    redisTemplate.delete(key);
                }
            }
            redisTemplate.delete(BaseConstants.USER_TOKEN + "-" + pkUser);
        }
    }

    /**
     * 根据来源平台+版本清理TOKEN白名单
     *
     * @param pkUser   用户主键
     * @param platform 来源平台
     * @param version  来源版本
     */
    public void cleanTokenAllow(long pkUser, String platform, String version) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            String token = "" + tokenMap.get(platform + "-" + version);
            tokenMap.remove(token);
            tokenMap.remove(platform + "-" + version);
            //重新设置以便刷新最后失效时间
            operations.set(BaseConstants.USER_TOKEN + "-" + pkUser, tokenMap, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        }
    }

    /**
     * 根据token清理TOKEN白名单
     *
     * @param pkUser 用户主键
     * @param token  TOKEN信息
     */
    public void cleanTokenAllow(long pkUser, String token) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(BaseConstants.USER_TOKEN + "-" + pkUser);
        if (exists) {
            HashMap<String, Object> tokenMap = operations.get(BaseConstants.USER_TOKEN + "-" + pkUser);
            HashMap<String, String> itemMap = (HashMap<String, String>) tokenMap.get(token);
            tokenMap.remove(token);
            //判断TOKEN的信息与平台版本存储的一致才进行删除，理论上会一致，但是不排除出现同平台的信息不一致的情况
            String tokenStr = "" + tokenMap.get(itemMap.get(BaseConstants.TOKEN_PLATFORM) + "-" + itemMap.get(BaseConstants.TOKEN_VERSION));
            if (token.equals(tokenStr)) {
                tokenMap.remove(itemMap.get(BaseConstants.TOKEN_PLATFORM) + "-" + itemMap.get(BaseConstants.TOKEN_VERSION));
            }
            //重新设置以便刷新最后失效时间
            operations.set(BaseConstants.USER_TOKEN + "-" + pkUser, tokenMap, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        }
    }

    /**
     * 设置Token的锁定状态
     *
     * @param token 用户Token
     */
    public void tokenLocked(String token, boolean locked) {
        ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(token);
        if (exists) {
            HashMap tokenItem = operations.get(token);
            if (locked) {
                tokenItem.put(TOKEN_STATUS, TOKEN_STATUS_LOCKED);
            } else {
                tokenItem.put(TOKEN_STATUS, TOKEN_STATUS_UNLOCKED);
            }
            operations.set(token, tokenItem, TOKEN_EXPIRED_DAY, TimeUnit.DAYS);
        }
    }


    /**
     * 设置用户权限信息
     *
     * @param userId          用户ID
     * @param resourceHashMap 用户权限信息MAP KEY为模块ID+类名+方法名
     * @return
     */
    public void setUserResource(long userId, HashMap<String, UserResourceDTO> resourceHashMap) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(USER_RESOURCE + userId)) {
            redisTemplate.delete(USER_RESOURCE + userId);
        }
        operations.set(USER_RESOURCE + userId, pubTools.objectToJson(resourceHashMap));
    }

    /**
     * 获取用户权限信息
     *
     * @param userId 用户ID
     * @return
     */
    public JSONObject getUserResource(long userId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(USER_RESOURCE + userId);
        if (exists) {

            return pubTools.jsonToObject(operations.get(USER_RESOURCE + userId), JSONObject.class);
        }
        return null;
    }


    /**
     * 设置用户菜单
     *
     * @param userId   用户主键
     * @param menuList 用户菜单列表
     */
    public void setUserMenu(long userId, List<UserMenuDTO> menuList) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(USER_MENU + userId)) {
            redisTemplate.delete(USER_MENU + userId);
        }

        if (null == menuList) {
            redisTemplate.delete(USER_MENU + userId);
        } else {
            operations.set(USER_MENU + userId, pubTools.objectToJson(menuList));
        }
    }


    /**
     * 获取用户菜单
     *
     * @param userId 用户ID
     * @return
     */
    public List getUserMenu(long userId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        boolean exists = redisTemplate.hasKey(USER_MENU + userId);
        if (exists) {

            Object list = operations.get(USER_MENU + userId);
            if (list instanceof List) {
                return (List<UserMenuDTO>) list;
            }

            Object o = JSON.parse(operations.get(USER_MENU + userId));

            List<UserMenuDTO> rList = new ArrayList<>();

            List data = JSON.toJavaObject((JSON) o, List.class);

            for (int i = 0; i < data.size(); i++) {
                UserMenuDTO item = ((JSONObject) data.get(i)).toJavaObject(UserMenuDTO.class);
                rList.add(item);

            }
            return rList;
        }
        return null;
    }

    /**
     * 通过请求信息获取用户TOKEN信息
     *
     * @param request 请求信息
     * @return
     */
    public BaseToken getTokenByRequest(HttpServletRequest request) {
        BaseToken baseToken = null;
        //获取用户TOKEN信息，默认情况均通过消息ID获取
        String token = request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase());
        if (StringUtils.isBlank(token)) {
            //消息ID未获取到的时候，多数是内部测试阶段，此时使用Token进行获取
            token = request.getHeader(BaseConstants.TOKEN_KEY);
            if (StringUtils.isBlank(token)) {
                throw new BaseException(StatusCode.TOKEN_INVALID);
            } else {
                baseToken = getTokenInfo(token);
            }
        } else {
            baseToken = getSessionInfo(token);
        }
        if (null == baseToken) {
            throw new BaseException(StatusCode.TOKEN_LOCKED);
        }
        //判断获取TOKEN，TOKEN本身存在问题，比如已经封存
        if (null != baseToken.getErrCode()) {
            throw new BaseException(baseToken.getErrCode());
        }

        return baseToken;
    }

    /**
     * 通过请求信息获取用户第三方授权公司
     *
     * @param request 请求信息
     * @return
     */
    public Long getPkGroupByRequest(HttpServletRequest request) {
        //获取第三方公司信息，默认情况均通过消息ID获取
        String token = request.getHeader(BaseConstants.OTHER_MESSAGE_ID.toLowerCase());
        if (StringUtils.isBlank(token)) {
            //消息ID未获取到的时候，多数是内部测试阶段，此时使用Token进行获取
            token = request.getHeader(BaseConstants.OTHER_TOKEN_KEY);
            if (StringUtils.isBlank(token)) {
                token = request.getHeader(BaseConstants.GLOBAL_KEY.toLowerCase());
                if (StringUtils.isBlank(token)) {
                    token = request.getHeader(BaseConstants.TOKEN_KEY);
                    if (StringUtils.isNotBlank(token)) {
                        return getTokenInfo(token).getPkGroup();
                    }

                } else {
                    return getSessionInfo(token).getPkGroup();
                }


            } else {
                return getPkGroupByOtherToken(token);
            }
        } else {
            return getPkGroupByOtherMessageId(token);
        }

        //返回一个不存在的ID
        return -99L;
    }


}
