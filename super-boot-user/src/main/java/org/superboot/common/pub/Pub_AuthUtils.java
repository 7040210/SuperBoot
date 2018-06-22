package org.superboot.common.pub;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.StatusCode;
import org.superboot.entity.dto.UserMenuDTO;
import org.superboot.entity.dto.UserResourceDTO;
import org.superboot.entity.jpa.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

/**
 * <b> 授权刷新功能 </b>
 * <p>
 * 功能描述:提供当菜单授权发生变更、角色授权发生变更、用户角色发生变更时刷新缓存中的权限信息
 * </p>
 */
@Component
@Async
public class Pub_AuthUtils {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 菜单编号长度
     */
    public static final int MENU_LEV = 3;

    /**
     * 实体管理者
     */
    @PersistenceContext
    public EntityManager entityManager;

    /**
     * JPA查询工厂类
     */
    public JPAQueryFactory queryFactory;

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


    @Autowired
    private Pub_RedisUtils redisUtils;


    /**
     * 根据菜单主键刷新权限信息
     *
     * @param pkMenu 菜单主键
     */
    public void reloadByPkMenu(long pkMenu) {
        QSuperbootRoleMenu qSuperbootRoleMenu = QSuperbootRoleMenu.superbootRoleMenu;
        QSuperbootUserRole qSuperbootUserRole = QSuperbootUserRole.superbootUserRole;
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;
        List<SuperbootUser> users = getQueryFactory().select(qSuperbootUser)
                .from(qSuperbootRoleMenu, qSuperbootUserRole, qSuperbootUser)
                .where(qSuperbootRoleMenu.pkRole.eq(qSuperbootUserRole.pkRole)
                        .and(qSuperbootUserRole.pkUser.eq(qSuperbootUser.pkUser))
                        .and(qSuperbootRoleMenu.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootUserRole.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootUser.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootRoleMenu.pkMenu.eq(pkMenu))
                ).fetch();
        if (null != users) {
            for (SuperbootUser impUser : users) {
                reloadByPkUser(impUser.getPkUser());
            }
        }
    }

    /**
     * 根据角色主键刷新权限信息
     *
     * @param pkRole 角色主角
     */
    public void reloadByPkRole(long pkRole) {
        QSuperbootUserRole qSuperbootUserRole = QSuperbootUserRole.superbootUserRole;
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;
        List<SuperbootUser> users = getQueryFactory().select(qSuperbootUser)
                .from(qSuperbootUserRole, qSuperbootUser)
                .where(qSuperbootUserRole.pkUser.eq(qSuperbootUser.pkUser)
                        .and(qSuperbootUserRole.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootUser.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootUserRole.pkRole.eq(pkRole))
                ).fetch();
        if (null != users) {
            for (SuperbootUser impUser : users) {
                reloadByPkUser(impUser.getPkUser());
            }
        }


    }

    /**
     * 根据用户主键刷新权限信息
     *
     * @param pkUser 用户主键
     */
    public void reloadByPkUser(long pkUser) {


        //获取用户所有有权限的接口信息
        List<UserResourceDTO> list = getUserResource(pkUser);


        if (null != list) {
            if (0 < list.size()) {
                HashMap<String, UserResourceDTO> userResourceHashMap = new HashMap<String, UserResourceDTO>(list.size());
                for (UserResourceDTO resource : list) {
                    //权限KEY为微服务ID+类名+方法名
                    String key = resource.getModuleId() + resource.getMethodPath() + resource.getMethodName();
                    userResourceHashMap.put(key, resource);
                }

                //将用户权限信息 存入缓存
                redisUtils.setUserResource(pkUser, userResourceHashMap);


            }
        }

        //获取用户菜单
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        QSuperbootRoleMenu qSuperbootRoleMenu = QSuperbootRoleMenu.superbootRoleMenu;
        QSuperbootUserRole qSuperbootUserRole = QSuperbootUserRole.superbootUserRole;

        //根据用户角色获取用户末级菜单
        List<UserMenuDTO> userMenuList = getQueryFactory().select(
                Projections.bean(
                        UserMenuDTO.class,
                        qSuperbootMenu.menuCode,
                        qSuperbootMenu.menuName,
                        qSuperbootMenu.menuType,
                        qSuperbootMenu.menuLev,
                        qSuperbootMenu.orderCode,
                        qSuperbootMenu.menuIco,
                        qSuperbootMenu.menuUrl,
                        qSuperbootMenu.isEnd,
                        qSuperbootMenu.pkMenu,
                        qSuperbootMenu.pkFMenu
                )
        ).from(qSuperbootMenu, qSuperbootRoleMenu, qSuperbootUserRole)
                .where(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootRoleMenu.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootUserRole.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootMenu.pkMenu.eq(qSuperbootRoleMenu.pkMenu))
                        .and(qSuperbootRoleMenu.pkRole.eq(qSuperbootUserRole.pkRole))
                        .and(qSuperbootUserRole.pkUser.eq(pkUser))
                )
                .groupBy(qSuperbootMenu.menuCode,
                        qSuperbootMenu.menuName,
                        qSuperbootMenu.menuType,
                        qSuperbootMenu.menuLev,
                        qSuperbootMenu.orderCode,
                        qSuperbootMenu.menuIco,
                        qSuperbootMenu.menuUrl,
                        qSuperbootMenu.isEnd,
                        qSuperbootMenu.pkMenu,
                        qSuperbootMenu.pkFMenu)
                .orderBy(qSuperbootMenu.orderCode.asc())
                .fetch();


        //暂时存储用户菜单内部编码，内部编码必须为一级三位，否则会出现问题，此处为了效率不推荐使用数据库递归方式，直接前台拿到所有节点的内部序号，一次性查询出来
        HashMap<String, String> menuCodeMap = new HashMap<>();
        for (UserMenuDTO userMenu : userMenuList) {
            String orderCode = userMenu.getOrderCode();
            if (orderCode.length() % 3 != 0) {
                throw new BaseException(StatusCode.MENU_OEDER_CODE_LENGTH_ERROR);
            }
            //拆分功能节点内部序号用于构建菜单
            for (int i = 0; i < orderCode.length() / MENU_LEV; i++) {
                String menuCode = orderCode.substring(0, (i + 1) * 3);
                if (!menuCodeMap.containsKey(menuCode)) {
                    menuCodeMap.put(menuCode, menuCode);
                }
            }
        }

        //获取用户全部菜单
        if (menuCodeMap.size() == 0) {
            userMenuList = null;
        } else {
            userMenuList = getQueryFactory().select(
                    Projections.bean(
                            UserMenuDTO.class,
                            qSuperbootMenu.menuCode,
                            qSuperbootMenu.menuName,
                            qSuperbootMenu.menuType,
                            qSuperbootMenu.menuLev,
                            qSuperbootMenu.orderCode,
                            qSuperbootMenu.menuIco,
                            qSuperbootMenu.menuUrl,
                            qSuperbootMenu.isEnd,
                            qSuperbootMenu.pkMenu,
                            qSuperbootMenu.pkFMenu
                    )
            ).from(qSuperbootMenu)
                    .where(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                            .and(qSuperbootMenu.orderCode
                                    .in(menuCodeMap.keySet().toArray(new String[0]))))
                    .orderBy(qSuperbootMenu.orderCode.asc())
                    .fetch();
        }
        redisUtils.setUserMenu(pkUser, userMenuList);


    }


    /**
     * 获取用户相关信息，后期登陆的时候 可以一起获取，如果效率低下后期可拆分部分功能为异步
     *
     * @param userId 用户ID
     * @return
     */
    public List<UserResourceDTO> getUserResource(long userId) {


        //接口资源信息
        QSuperbootResource res = QSuperbootResource.superbootResource;
        //权限信息
        QSuperbootPermissions ip = QSuperbootPermissions.superbootPermissions;
        //权限资源信息
        QSuperbootPermissionsResource ips = QSuperbootPermissionsResource.superbootPermissionsResource;
        //菜单权限信息
        QSuperbootMenuPermissions imp = QSuperbootMenuPermissions.superbootMenuPermissions;
        //菜单新
        QSuperbootMenu im = QSuperbootMenu.superbootMenu;
        //角色菜单信息
        QSuperbootRoleMenu irm = QSuperbootRoleMenu.superbootRoleMenu;
        //角色信息
        QSuperbootRole ir = QSuperbootRole.superbootRole;
        //用户角色
        QSuperbootUserRole iur = QSuperbootUserRole.superbootUserRole;
        //用户信息
        QSuperbootUser iu = QSuperbootUser.superbootUser;

        return getQueryFactory().select(Projections.bean(
                UserResourceDTO.class,
                res.methodName,
                res.methodPath,
                res.modulePath,
                res.moduleId,
                res.url,
                ip.permissionsCode,
                ip.permissionsName,
                im.menuCode,
                im.menuName,
                im.menuType,
                im.menuUrl,
                im.orderCode,
                ir.roleCode,
                ir.roleName,
                ir.roleType
        )).from(res, ips, ip, imp, im, irm, ir, iur, iu)
                .where(res.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(ip.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(ips.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(imp.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(im.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(irm.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(ir.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(iur.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(iu.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(res.pkResource.eq(ips.pkResource))
                        .and(ips.pkPermissions.eq(ip.pkPermissions))
                        .and(ip.pkPermissions.eq(imp.pkPermissions))
                        .and(im.pkMenu.eq(irm.pkMenu))
                        .and(imp.pkMenu.eq(im.pkMenu))
                        .and(irm.pkRole.eq(ir.pkRole))
                        .and(ir.pkRole.eq(iur.pkRole))
                        .and(iur.pkUser.eq(iu.pkUser))
                        .and(iu.pkUser.eq(userId)))
                //按照菜单内部排序号返回
                .orderBy(im.orderCode.asc())
                .fetch();
    }

}
