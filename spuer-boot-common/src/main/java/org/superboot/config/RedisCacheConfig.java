package org.superboot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Arrays;

/**
 * <b> 重写缓存KEY生成逻辑 </b>
 * <p>
 * 功能描述:
 * * 注意：RedisCacheConfig这里也可以不用继承：CachingConfigurerSupport，也就是直接一个普通的Class就好了；
 * <p>
 * 这里主要我们之后要重新实现 key的生成策略，只要这里修改KeyGenerator，其它位置不用修改就生效了。
 * <p>
 * 普通使用普通类的方式的话，那么在使用@Cacheable的时候还需要指定KeyGenerator的名称;这样编码的时候比较麻烦。
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 17:50
 * @Path org.superboot.config.RedisCacheConfig
 */
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    /**
     * RedisTemplate缓存操作类,类似于jdbcTemplate的一个类;
     * <p>
     * 虽然CacheManager也能获取到Cache对象，但是操作起来没有那么灵活；
     * <p>
     * 这里在扩展下：RedisTemplate这个类不见得很好操作，我们可以在进行扩展一个我们
     * <p>
     * 自己的缓存类，比如：RedisStorage类;
     *
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(jedisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 定义缓存对象
     *
     * @return
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        String[] cacheNames = {"default", "users", "roles", "apis", "configs", "info"};
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate(), Arrays.asList(cacheNames));
        redisCacheManager.setDefaultExpiration(86400);
        return redisCacheManager;
    }

    /**
     * 指定默认的缓存存储位置
     *
     * @return
     */
    @Bean
    public Cache cache() {
        return cacheManager().getCache("default");
    }


    /**
     * 自定义key.
     * 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName());
            sb.append(method.getName());
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            logger.debug("Redis KEY =" + sb.toString());
            return sb.toString();
        };

    }

}
