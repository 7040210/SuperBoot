package org.superboot.dao.jpa;

import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.superboot.base.BaseJpaDAO;
import org.superboot.entity.jpa.QSuperbootMenu;
import org.superboot.entity.jpa.SuperbootMenu;

import java.util.List;

/**
 * <b> 菜单管理数据库操作DAO类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Repository
@CacheConfig(cacheNames = "menus")
public interface MenuDAO extends BaseJpaDAO<SuperbootMenu>, QuerydslBinderCustomizer<QSuperbootMenu> {


    /**
     * 增加对查询条件的模糊搜索支持
     *
     * @param bindings
     * @param menu
     */
    @Override
    default void customize(QuerydslBindings bindings, QSuperbootMenu menu) {
        bindings.bind(menu.menuCode).first(StringExpression::containsIgnoreCase);
        bindings.bind(menu.menuName).first(StringExpression::containsIgnoreCase);
    }

    /**
     * 根据菜单编码查询菜单信息
     *
     * @param menuCode 菜单编码
     * @param dr       删除标识
     * @return
     */
    @Cacheable(key = "#p0+#p1")
    SuperbootMenu findByMenuCodeAndDr(String menuCode, int dr);

    /**
     * 获取某个菜单下的所有下级菜单
     *
     * @param fmenu 上级菜单主键
     * @param dr    删除标识
     * @return
     */
    @Cacheable(key = "#p0+#p1")
    List<SuperbootMenu> findByPkFMenuAndDr(long fmenu, int dr);


}
