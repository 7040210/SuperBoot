package org.superboot.service.impl;

import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_AuthUtils;
import org.superboot.dao.jpa.MenuPermissionsDAO;
import org.superboot.entity.dto.MenuAuthDTO;
import org.superboot.entity.jpa.QSuperbootMenuPermissions;
import org.superboot.entity.jpa.QSuperbootPermissions;
import org.superboot.entity.jpa.SuperbootMenuPermissions;
import org.superboot.entity.request.ReqMenuAuth;
import org.superboot.entity.response.ResPermissions;
import org.superboot.service.MenuAuthService;

import java.util.List;

/**
 * <b> 菜单授权 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class MenuAuthServiceImpl extends BaseService implements MenuAuthService {

    @Autowired
    private MenuPermissionsDAO menuPermissionsDAO;

    @Autowired
    private Pub_AuthUtils authUtils;

    @Override
    public BaseResponse addMenuAuth(ReqMenuAuth reqMenuAuth) throws BaseException {
        for (MenuAuthDTO menuAuth : reqMenuAuth.getMenuAuths()) {
            if (null == menuPermissionsDAO.findByPkMenuAndPkPermissionsAndDr(menuAuth.getPkMenu(), menuAuth.getPkPermissions(), BaseConstants.DATA_STATUS_OK)) {
                SuperbootMenuPermissions impMenuPermissions = new SuperbootMenuPermissions();
                BeanUtils.copyProperties(menuAuth, impMenuPermissions);
                menuPermissionsDAO.save(impMenuPermissions);

                //刷新菜单的授权信息
                authUtils.reloadByPkMenu(menuAuth.getPkMenu());
            }
        }
        return new BaseResponse(StatusCode.AUTH_SUCCESS);
    }

    @Override
    public BaseResponse delMenuAuth(long menuPermissionsId) throws BaseException {
        SuperbootMenuPermissions impMenuPermissions = menuPermissionsDAO.findOne(menuPermissionsId);
        if (null != impMenuPermissions) {
            impMenuPermissions.setDr(BaseConstants.DATA_STATUS_DEL);
            menuPermissionsDAO.save(impMenuPermissions);

            //刷新菜单的授权信息
            authUtils.reloadByPkMenu(impMenuPermissions.getPkMenu());
        }

        return new BaseResponse(StatusCode.UNAUTH_SUCCESS);
    }

    @Override
    public BaseResponse getMenuAuthList(long menuId) throws BaseException {

        QSuperbootPermissions permissions = QSuperbootPermissions.superbootPermissions;

        QSuperbootMenuPermissions menuPermissions = QSuperbootMenuPermissions.superbootMenuPermissions;

        List<ResPermissions> list = getQueryFactory()
                .select(
                        Projections.bean(
                                ResPermissions.class,
                                permissions.pkPermissions,
                                permissions.permissionsCode,
                                permissions.permissionsName,
                                permissions.permissionsInfo
                        ))
                .from(permissions, menuPermissions)
                .where(permissions.pkPermissions.eq(menuPermissions.pkPermissions)
                        .and(menuPermissions.pkMenu.eq(menuId)))
                .fetch();

        return new BaseResponse(list);
    }
}
