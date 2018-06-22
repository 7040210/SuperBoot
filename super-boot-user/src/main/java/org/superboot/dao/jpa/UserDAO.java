package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootUser;
import org.superboot.entity.jpa.SuperbootUser;

/**
 * <b> 用户操作DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Repository
@CacheConfig(cacheNames = "users")
public interface UserDAO extends BaseJpaDAO<SuperbootUser>, QuerydslBinderCustomizer<QSuperbootUser> {
    /**
     * 根据用户主键和删除状态查找
     *
     * @param pkUser
     * @param dr
     * @return
     */
    SuperbootUser findByPkUserAndDr(long pkUser, int dr);

    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param user
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootUser user) {
        bindings.bind(user.userCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(user.userEmail).first(StringExpression::containsIgnoreCase);
        bindings.bind(user.userPhone).first(StringExpression::containsIgnoreCase);
    }


    /**
     * 按照账户查询用户
     *
     * @param userCode
     * @param dr
     * @return
     */
    SuperbootUser findByUserCodeAndDr(String userCode, int dr);


    /**
     * 按照邮箱查询用户
     *
     * @param email
     * @param dr
     * @return
     */
    SuperbootUser findByUserEmailAndDr(String email, int dr);


    /**
     * 安装手机号查询用户
     *
     * @param phone
     * @param dr
     * @return
     */
    SuperbootUser findByUserPhoneAndDr(String phone, int dr);


}
