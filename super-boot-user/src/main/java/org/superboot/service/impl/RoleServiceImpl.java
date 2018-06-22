package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_AuthUtils;
import org.superboot.dao.jpa.RoleDAO;
import org.superboot.dao.jpa.RoleMenuDAO;
import org.superboot.dao.jpa.UserRoleDAO;
import org.superboot.entity.dto.RoleMenuDTO;
import org.superboot.entity.dto.RoleUserDTO;
import org.superboot.entity.jpa.*;
import org.superboot.entity.request.ReqRole;
import org.superboot.entity.request.ReqRoleMenu;
import org.superboot.entity.request.ReqRoleUser;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResRole;
import org.superboot.service.RoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * <b> 提供基于角色的相关操作 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private RoleMenuDAO roleMenuDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    private Pub_AuthUtils authUtils;

    @Override
    public BaseResponse addRole(long groupId, ReqRole role) {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(groupId);
        if (null != roleDAO.findByRoleCodeAndPkGroupAndDr(role.getRoleCode(), groupId, BaseConstants.DATA_STATUS_OK)) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS);
        }
        SuperbootRole impRole = new SuperbootRole();
        BeanUtils.copyProperties(role, impRole);

        impRole.setRoleType(1);
        if (-1 == groupId) {
            impRole.setRoleType(0);
        }
        impRole.setPkGroup(groupId);
        impRole = roleDAO.save(impRole);

        ResRole resRole = new ResRole();
        BeanUtils.copyProperties(impRole, resRole);

        return new BaseResponse(StatusCode.ADD_SUCCESS, resRole);
    }

    @Override
    public BaseResponse addRoleMenu(ReqRoleMenu roleMenuList) throws BaseException {
        QSuperbootMenu qSuperbootMenu = QSuperbootMenu.superbootMenu;
        QSuperbootRoleMenu qSuperbootRoleMenu = QSuperbootRoleMenu.superbootRoleMenu;
        for (RoleMenuDTO roleMenu : roleMenuList.getRoleMenus()) {
            List<SuperbootRoleMenu> rm = getQueryFactory().selectFrom(qSuperbootRoleMenu).where(
                    qSuperbootRoleMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                            .and(qSuperbootRoleMenu.pkMenu.eq(roleMenu.getPkMenu()))
                            .and(qSuperbootRoleMenu.pkRole.eq(roleMenu.getPkRole()))
            ).fetch();
            if (null == rm || 0 == rm.size()) {
                List<SuperbootMenu> menus = getQueryFactory().selectFrom(qSuperbootMenu).where(
                        qSuperbootMenu.dr.eq(BaseConstants.DATA_STATUS_OK)
                                .and(qSuperbootMenu.menuCode.startsWith(
                                        JPAExpressions.select(qSuperbootMenu.menuCode).from(qSuperbootMenu).where(
                                                qSuperbootMenu.pkMenu.eq(roleMenu.getPkMenu())
                                        )
                                ))
                ).fetch();
                List<SuperbootRoleMenu> roleMenus = new ArrayList<>();
                for (SuperbootMenu menu : menus) {
                    SuperbootRoleMenu impRoleMenu = new SuperbootRoleMenu();
                    impRoleMenu.setPkMenu(menu.getPkMenu());
                    impRoleMenu.setPkRole(roleMenu.getPkRole());
                    roleMenus.add(impRoleMenu);
                }
                if (0 < roleMenus.size()) {
                    roleMenuDAO.save(roleMenus);
                }
                //刷新角色的缓存信息
                authUtils.reloadByPkRole(roleMenu.getPkRole());
            }
        }
        return new BaseResponse(StatusCode.AUTH_SUCCESS);
    }

    @Override
    public BaseResponse addRoleUser(ReqRoleUser roleUserList) throws BaseException {
        for (RoleUserDTO roleUser : roleUserList.getUsers()) {
            if (null == userRoleDAO.findByPkRoleAndPkUserAndDr(roleUser.getPkRole(), roleUser.getPkUser(), BaseConstants.DATA_STATUS_OK)) {
                SuperbootUserRole impUserRole = new SuperbootUserRole();
                BeanUtils.copyProperties(roleUser, impUserRole);
                userRoleDAO.save(impUserRole);
                //刷新用户角色信息
                authUtils.reloadByPkUser(roleUser.getPkUser());
            }
        }
        return new BaseResponse(StatusCode.AUTH_SUCCESS);
    }

    @Override
    public BaseResponse getCount(long pkGroup, Predicate predicate) throws BaseException {
        QSuperbootRole qSuperbootRole = QSuperbootRole.superbootRole;
        long count;
        if (-1 == pkGroup) {
            count = getQueryFactory().
                    select(qSuperbootRole.pkRole.count())
                    .from(qSuperbootRole)
                    .where(qSuperbootRole.pkGroup.in(-1L).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)).and(qSuperbootRole.roleType.eq(0)).and(predicate))
                    .fetchOne();

        } else {
            checkGroupIsMy(pkGroup);
            count = getQueryFactory().
                    select(qSuperbootRole.pkRole.count())
                    .from(qSuperbootRole)
                    .where(qSuperbootRole.pkGroup.eq(getPkGroup()).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)).and(qSuperbootRole.roleType.eq(1)).and(predicate))
                    .fetchOne();
        }
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse getRoleAll(long pkGroup, Predicate predicate) throws BaseException {
        QSuperbootRole qSuperbootRole = QSuperbootRole.superbootRole;
        if (-1 == pkGroup) {
            //-1企业默认只查
            List list = getQueryFactory()
                    .select(Projections.bean(
                            ResRole.class,
                            qSuperbootRole.roleCode,
                            qSuperbootRole.roleName,
                            qSuperbootRole.roleInfo,
                            qSuperbootRole.roleType,
                            qSuperbootRole.pkGroup,
                            qSuperbootRole.pkRole
                    ))
                    .from(qSuperbootRole)
                    .where(qSuperbootRole.pkGroup.eq(getPkGroup())
                            .and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK))
                            .and(qSuperbootRole.roleType.eq(0))
                            .and(qSuperbootRole.pkRole.notIn(377385783532716032L))
                            .and(predicate))
                    .orderBy(qSuperbootRole.roleCode.asc())
                    .fetch();
            //使用自定义方式查询结果
            return new BaseResponse(list);
        }
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        List list = getQueryFactory()
                .select(Projections.bean(
                        ResRole.class,
                        qSuperbootRole.roleCode,
                        qSuperbootRole.roleName,
                        qSuperbootRole.roleInfo,
                        qSuperbootRole.roleType,
                        qSuperbootRole.pkGroup,
                        qSuperbootRole.pkRole
                ))
                .from(qSuperbootRole)
                .where(qSuperbootRole.pkGroup.eq(getPkGroup()).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)).and(qSuperbootRole.roleType.eq(1)).and(predicate))
                .orderBy(qSuperbootRole.roleCode.asc())
                .fetch();

        //使用自定义方式查询结果
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse delRole(long roleId) throws BaseException {
        QSuperbootRole qSuperbootRole = QSuperbootRole.superbootRole;
        SuperbootRole role = roleDAO.findOne(qSuperbootRole.pkRole.eq(roleId).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)));
        if (null != role) {
            checkGroupIsMy(role.getPkGroup());
            //判断角色是否被用户引用
            QSuperbootUserRole userRole = QSuperbootUserRole.superbootUserRole;
            long count = getQueryFactory().select(userRole.pkRole.count()).from(userRole).where(
                    userRole.pkRole.eq(roleId).and(userRole.dr.eq(BaseConstants.DATA_STATUS_OK))
            ).fetchOne();
            if (0 < count) {
                throw new BaseException(StatusCode.DATA_QUOTE, "角色被用户引用，无法删除");
            }
            role.setDr(BaseConstants.DATA_STATUS_DEL);
            roleDAO.save(role);
        }
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    @Override
    public BaseResponse setRole(Long roleId, ReqRole reqRole) throws BaseException {
        SuperbootRole role = roleDAO.findOne(roleId);
        if (null != role) {
            checkGroupIsMy(role.getPkGroup());
            BeanUtils.copyProperties(reqRole, role);
            role = roleDAO.save(role);
            ResRole resRole = new ResRole();
            BeanUtils.copyProperties(role, resRole);

            return new BaseResponse(StatusCode.ADD_SUCCESS, resRole);
        }
        return new BaseResponse(StatusCode.DATA_NOT_FNID);
    }


    @Override
    public BaseResponse getRoles(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException {

        QSuperbootRole qSuperbootRole = QSuperbootRole.superbootRole;
        if (-1 == pkGroup) {
            List list = getQueryFactory()
                    .select(Projections.bean(
                            ResRole.class,
                            qSuperbootRole.roleCode,
                            qSuperbootRole.roleName,
                            qSuperbootRole.roleInfo,
                            qSuperbootRole.roleType,
                            qSuperbootRole.pkGroup,
                            qSuperbootRole.pkRole
                    ))
                    .from(qSuperbootRole)
                    .where(qSuperbootRole.pkGroup.eq(getPkGroup()).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)).and(qSuperbootRole.roleType.eq(0)).and(predicate))
                    .orderBy(qSuperbootRole.roleCode.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new BaseResponse(list);
        }
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        List list = getQueryFactory()
                .select(Projections.bean(
                        ResRole.class,
                        qSuperbootRole.roleCode,
                        qSuperbootRole.roleName,
                        qSuperbootRole.roleInfo,
                        qSuperbootRole.roleType,
                        qSuperbootRole.pkGroup,
                        qSuperbootRole.pkRole
                ))
                .from(qSuperbootRole)
                .where(qSuperbootRole.pkGroup.eq(getPkGroup()).and(qSuperbootRole.dr.eq(BaseConstants.DATA_STATUS_OK)).and(qSuperbootRole.roleType.eq(1)).and(predicate))
                .orderBy(qSuperbootRole.roleCode.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //使用自定义方式查询结果
        return new BaseResponse(list);
    }

}
