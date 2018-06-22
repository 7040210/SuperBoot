package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_AuthUtils;
import org.superboot.dao.jpa.MenuDAO;
import org.superboot.entity.jpa.QSuperbootMenu;
import org.superboot.entity.jpa.QSuperbootRoleMenu;
import org.superboot.entity.jpa.SuperbootMenu;
import org.superboot.entity.request.ReqMenu;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResMenu;
import org.superboot.service.MenuService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <b> 菜单管理具体实现类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class MenuServiceImpl extends BaseService implements MenuService {

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private Pub_AuthUtils authUtils;


    @Override
    public BaseResponse addMenu(ReqMenu menu) throws BaseException {

        if (null != menuDAO.findByMenuCodeAndDr(menu.getMenuCode(), BaseConstants.DATA_STATUS_OK)) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS);
        }

        SuperbootMenu impMenu = new SuperbootMenu();
        BeanUtils.copyProperties(menu, impMenu);
        impMenu.setIsEnd(BaseConstants.TRUE);
        if (0 == menu.getPkFMenu()) {
            impMenu.setMenuLev(1);
        } else {
            SuperbootMenu fmenu = menuDAO.findOne(menu.getPkFMenu());
            if (0 != menu.getPkFMenu() && null == fmenu) {
                throw new BaseException(StatusCode.MENU_F_NOT_FIND);
            }
            //设置级别为上级级别加一
            impMenu.setMenuLev(fmenu.getMenuLev() + 1);
            //更新为非末级
            if (BaseConstants.TRUE.equals(fmenu.getIsEnd())) {
                fmenu.setIsEnd(BaseConstants.FALSE);
                menuDAO.save(fmenu);
            }
        }
        impMenu = menuDAO.save(impMenu);
        ResMenu resMenu = new ResMenu();
        if (null != impMenu) {
            BeanUtils.copyProperties(impMenu, resMenu);
        }

        return new BaseResponse(StatusCode.ADD_SUCCESS, resMenu);
    }

    @Override
    public BaseResponse setMenu(long menuId, ReqMenu menu) throws BaseException {
        SuperbootMenu impMenu = menuDAO.findOne(menuId);
        if (null != impMenu) {
            BeanUtils.copyProperties(menu, impMenu);
            impMenu = menuDAO.save(impMenu);
            //刷新菜单的授权信息
            authUtils.reloadByPkMenu(impMenu.getPkMenu());
        }

        ResMenu resMenu = new ResMenu();
        if (null != impMenu) {
            BeanUtils.copyProperties(impMenu, resMenu);
        }

        return new BaseResponse(StatusCode.UPDATE_SUCCESS, resMenu);
    }

    @Override
    public BaseResponse delMenu(long menuId) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        SuperbootMenu impMenu = menuDAO.findOne(qSuperbootMenu.pkMenu.eq(menuId).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)));
        if (null != impMenu) {
            //判断菜单是否被角色引用
            QSuperbootRoleMenu roleMenu = QSuperbootRoleMenu.superbootRoleMenu;
            long rms = getQueryFactory().select(roleMenu.count()).from(roleMenu).where(
                    roleMenu.pkMenu.eq(menuId).and(roleMenu.dr.eq(BaseConstants.DATA_STATUS_OK))
            ).fetchOne();
            if (0 < rms) {
                throw new BaseException(StatusCode.DATA_QUOTE, "菜单被角色引用，无法删除");
            }
            //判断菜单是否存在下级
            if (!BaseConstants.TRUE.equals(impMenu.getIsEnd())) {
                throw new BaseException(StatusCode.DATA_QUOTE, "菜单存在下级菜单，无法删除");
            }
            impMenu.setDr(BaseConstants.DATA_STATUS_DEL);
            menuDAO.save(impMenu);
            //刷新菜单的授权信息
            authUtils.reloadByPkMenu(impMenu.getPkMenu());

            //处理上级菜单是否还有末级 如果没有则将状态置为末级
            long count = menuDAO.count(qSuperbootMenu.pkFMenu.eq(impMenu.getPkFMenu()).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)));
            if (0 == count) {
                SuperbootMenu fmenu = menuDAO.findOne(impMenu.getPkFMenu());
                if (null != fmenu) {
                    fmenu.setIsEnd(BaseConstants.TRUE);
                    menuDAO.save(fmenu);
                }
            }
        }
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    @Override
    public BaseResponse getMenu(long menuId) throws BaseException {
        SuperbootMenu menu = menuDAO.findOne(menuId);
        ResMenu resMenu = new ResMenu();
        if (null != menu) {
            BeanUtils.copyProperties(menu, resMenu);
        }
        return new BaseResponse(resMenu);
    }


    @Override
    public BaseResponse getCount(long pkGroup, Predicate predicate) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        long count;
        if (-1 == pkGroup) {
            count = getQueryFactory().select(qSuperbootMenu.pkMenu.count()).from(qSuperbootMenu)
                    .where(qSuperbootMenu.menuType.in(0, 1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate)).fetchOne();
        } else {
            //校验操作的组织是否是自己的组织
            checkGroupIsMy(pkGroup);
            count = getQueryFactory().select(qSuperbootMenu.pkMenu.count()).from(qSuperbootMenu)
                    .where(qSuperbootMenu.menuType.eq(1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate)).fetchOne();
        }
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse<List<ResMenu>> getMenusByRole(Long pkRole) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        QSuperbootRoleMenu qSuperbootRoleMenu = QSuperbootRoleMenu.superbootRoleMenu;
        List<Integer> menuType = new ArrayList<>();
        menuType.add(1);
        if (null == getPkGroup() || -1 == getPkGroup()) {
            menuType.add(0);
        }
        List<ResMenu> res = getQueryFactory().select(Projections.bean(
                ResMenu.class,
                qSuperbootMenu.menuCode,
                qSuperbootMenu.menuName,
                qSuperbootMenu.menuType,
                qSuperbootMenu.pkMenu,
                qSuperbootMenu.pkFMenu,
                qSuperbootMenu.menuUrl,
                qSuperbootMenu.orderCode,
                qSuperbootMenu.menuIco,
                qSuperbootMenu.isEnd

        ))
                .from(qSuperbootMenu, qSuperbootRoleMenu)
                .where(
                        qSuperbootMenu.menuType.in(menuType)
                                .and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK))
                                .and(qSuperbootMenu.pkMenu.eq(qSuperbootRoleMenu.pkMenu))
                                .and(qSuperbootRoleMenu.dr.eq(qSuperbootMenu.dr))
                                .and(qSuperbootRoleMenu.pkRole.eq(pkRole))
                )
                .orderBy(qSuperbootMenu.orderCode.asc())
                .fetch();
        return new BaseResponse<>(res);
    }

    @Override
    public BaseResponse<List<ResMenu>> getRoleNoMenu(Long pkRole, Long pkGroup, Pageable pageable, Predicate predicate) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        QSuperbootRoleMenu qSuperbootRoleMenu = QSuperbootRoleMenu.superbootRoleMenu;
        JPAQuery authMenu = getQueryFactory().select(qSuperbootRoleMenu.pkMenu).from(qSuperbootRoleMenu)
                .where(qSuperbootRoleMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootRoleMenu.pkRole.eq(pkRole))
                );
        if (-1 == pkGroup) {
            List list = getQueryFactory().select(Projections.bean(
                    ResMenu.class,
                    qSuperbootMenu.menuCode,
                    qSuperbootMenu.menuName,
                    qSuperbootMenu.menuType,
                    qSuperbootMenu.pkMenu,
                    qSuperbootMenu.pkFMenu,
                    qSuperbootMenu.menuUrl,
                    qSuperbootMenu.orderCode,
                    qSuperbootMenu.menuIco,
                    qSuperbootMenu.isEnd

            ))
                    .from(qSuperbootMenu)
                    .where(qSuperbootMenu.menuType.in(0, 1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate)
                            .and(qSuperbootMenu.pkMenu.notIn(authMenu))
                    )
                    .orderBy(qSuperbootMenu.orderCode.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            //使用自定义方式查询结果
            return new BaseResponse(list);
        }
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        List list = getQueryFactory().select(Projections.bean(
                ResMenu.class,
                qSuperbootMenu.menuCode,
                qSuperbootMenu.menuName,
                qSuperbootMenu.menuType,
                qSuperbootMenu.pkFMenu,
                qSuperbootMenu.pkMenu,
                qSuperbootMenu.menuUrl,
                qSuperbootMenu.orderCode,
                qSuperbootMenu.menuIco,
                qSuperbootMenu.isEnd

        ))
                .from(qSuperbootMenu)
                .where(qSuperbootMenu.menuType.in(1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate)
                        .and(qSuperbootMenu.pkMenu.notIn(authMenu))
                )
                .orderBy(qSuperbootMenu.orderCode.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new BaseResponse<>(list);
    }

    @Override
    public BaseResponse<ResCount> cheReportMenu(Long pkTemplate) throws BaseException {
        QSuperbootMenu qMenu = QSuperbootMenu.superbootMenu;
        long res = getQueryFactory().selectFrom(qMenu).where(
                qMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qMenu.menuUrl.endsWith(pkTemplate.toString()))
        ).fetchCount();
        return new BaseResponse<>(new ResCount(res));
    }

    @Override
    public BaseResponse getMenus(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        if (-1 == pkGroup) {
            List list = getQueryFactory().select(Projections.bean(
                    ResMenu.class,
                    qSuperbootMenu.menuCode,
                    qSuperbootMenu.menuName,
                    qSuperbootMenu.menuType,
                    qSuperbootMenu.pkMenu,
                    qSuperbootMenu.pkFMenu,
                    qSuperbootMenu.menuUrl,
                    qSuperbootMenu.orderCode,
                    qSuperbootMenu.menuIco,
                    qSuperbootMenu.isEnd

            ))
                    .from(qSuperbootMenu)
                    .where(qSuperbootMenu.menuType.in(0, 1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate))
                    .orderBy(qSuperbootMenu.orderCode.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            //使用自定义方式查询结果
            return new BaseResponse(list);
        }
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        List list = getQueryFactory().select(Projections.bean(
                ResMenu.class,
                qSuperbootMenu.menuCode,
                qSuperbootMenu.menuName,
                qSuperbootMenu.menuType,
                qSuperbootMenu.pkFMenu,
                qSuperbootMenu.pkMenu,
                qSuperbootMenu.menuUrl,
                qSuperbootMenu.orderCode,
                qSuperbootMenu.menuIco,
                qSuperbootMenu.isEnd

        ))
                .from(qSuperbootMenu)
                .where(qSuperbootMenu.menuType.in(1).and(qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)).and(predicate))
                .orderBy(qSuperbootMenu.orderCode.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new BaseResponse(list);
    }

    /**
     * 用于构造前端返回数据，后期需要改造为实体类，此处只是为了实现写法
     *
     * @param tuple
     * @return
     */
    public Map<String, Object> getMenuMap(SuperbootMenu tuple) {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(qSuperbootMenu.menuCode.getMetadata().getName(), tuple.getMenuCode());
        map.put(qSuperbootMenu.menuName.getMetadata().getName(), tuple.getMenuName());
        map.put(qSuperbootMenu.menuUrl.getMetadata().getName(), tuple.getMenuUrl());
        map.put(qSuperbootMenu.menuIco.getMetadata().getName(), tuple.getMenuIco());
        map.put(qSuperbootMenu.pkMenu.getMetadata().getName(), tuple.getPkMenu());
        map.put(qSuperbootMenu.pkFMenu.getMetadata().getName(), tuple.getPkFMenu());
        map.put(qSuperbootMenu.isEnd.getMetadata().getName(), tuple.getIsEnd());
        return map;
    }
}
