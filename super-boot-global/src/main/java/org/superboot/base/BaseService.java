package org.superboot.base;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.superboot.common.pub.Pub_RedisUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <b> Service基类 </b>
 * <p>
 * 功能描述:主要目的为了实现基于DSL的高级查询以及对事务的支持，另外提供一些通用的方法逻辑
 * </p>
 */
@Component
@Transactional(rollbackFor = BaseException.class)
public class BaseService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 实体管理者
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 缓存管理类
     */
    @Autowired
    private Pub_RedisUtils redisUtils;

    /**
     * JPA查询工厂类
     */
    private JPAQueryFactory queryFactory;

    /**
     * 在类初始化的时候对JPAQueryFactory进行初始化操作
     */
    private void initFactory() {
        if (null == queryFactory) {
            queryFactory = new JPAQueryFactory(entityManager);
        }
    }

    /**
     * 获取DSL语法支持
     *
     * @return
     */
    public JPAQueryFactory getQueryFactory() {
        initFactory();
        return queryFactory;
    }

    /**
     * 获取缓存操作类
     *
     * @return
     */
    public Pub_RedisUtils getRedisUtils() {
        return redisUtils;
    }

    /**
     * 检测当前用户与请求的组织是否一致主要用于判断是否存在本组织操作其他组织数据的情况
     *
     * @param pkGroup
     */
    public void checkGroupIsMy(long pkGroup) throws BaseException {
        if (getPkGroup() != pkGroup) {
            throw new BaseException(StatusCode.UNAUTHORIZED_OPERATION);
        }

    }

    /**
     * 获取用户TOKEN信息
     *
     * @return
     */
    public BaseToken getBaseToken() {
        //通过上下文获取会话信息
        ServletRequestAttributes attributes = getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        BaseToken baseToken = redisUtils.getTokenByRequest(request);
        return baseToken;
    }


    /**
     * 根据第三方授权信息获取公司主键
     *
     * @return
     */
    public Long getPkGroup() {
        //通过上下文获取会话信息
        ServletRequestAttributes attributes = getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return redisUtils.getPkGroupByRequest(request);
    }

    /**
     * 获取WEB请求上下文信息
     *
     * @return
     */
    public ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取可查询属性所属组织
     *
     * @param pkGroup
     * @return
     * @throws BaseException
     */
    protected List<Long> getItemPkGroups(long pkGroup) throws BaseException {
        List<Long> res = new ArrayList<>();
        if (-1 != pkGroup) {
            res.add(-1L);
        }
        res.add(pkGroup);
        return res;
    }

}
