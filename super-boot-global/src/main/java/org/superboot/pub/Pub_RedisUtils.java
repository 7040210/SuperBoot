package org.superboot.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.superboot.base.BaseConstants;

import java.text.ParseException;

/**
 * <b> 全局Redis工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/10/4
 * @time 11:45
 * @Path org.superboot.pub.Pub_RedisUtils
 */
@Component
public class Pub_RedisUtils {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Pub_Tools pubTools;

    /**
     * 设置微服务之间业务调用信任ID信息
     *
     * @return
     */
    public String setGlobalID() throws ParseException {
        if (!stringRedisTemplate.hasKey(BaseConstants.GLOBAL_KEY)) {
            String key = pubTools.genUUID()+"";
            //向redis里存入数据
            stringRedisTemplate.opsForValue().set(BaseConstants.GLOBAL_KEY,key );
            return key;
        }else{
            String value = stringRedisTemplate.opsForValue().get(BaseConstants.GLOBAL_KEY);
            return value;
        }
    }

    /**
     * 校验微服务之间业务调用信任ID
     *
     * @return
     */
    public boolean checkGlobalID(String id) {
        if (stringRedisTemplate.hasKey(BaseConstants.GLOBAL_KEY)) {
            String value = stringRedisTemplate.opsForValue().get(BaseConstants.GLOBAL_KEY);
            return id.equals(value);
        }
        return false;
    }
}
