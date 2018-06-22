package org.superboot.controller.sys;

import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqMenuAuth;
import org.superboot.entity.response.ResPermissions;
import org.superboot.service.MenuAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b> 系统菜单授权 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@RequestMapping("/sys/menu/auth")
@RestController
@Api(tags = "用户中心系统菜单授权接口", description = "提供对系统菜单授权的维护管理")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME,BaseConstants.DEV_USER_NAME,BaseConstants.OPER_USER_NAME})
public class SysMenuAuthController {

    @Autowired
    private MenuAuthService menuAuthService;

    @ApiOperation(value = "菜单授权", notes = "菜单授权，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BaseResponse addMenuAuth(@RequestBody @Validated ReqMenuAuth auth) throws BaseException {
        return menuAuthService.addMenuAuth(auth);
    }


    @ApiOperation(value = "取消菜单授权", notes = "取消菜单授权，系统管理员默认可以访问")
    @RequestMapping(value = "/{menuPermissionsId}", method = RequestMethod.DELETE)
    public BaseResponse delMenuAuth(@PathVariable("menuPermissionsId") Long menuPermissionsId) throws BaseException {
        return menuAuthService.delMenuAuth(menuPermissionsId);
    }

    @ApiOperation(value = "获取菜单授权列表", notes = "获取菜单授权接口列表，主要用于取消授权使用")
    @RequestMapping(value = "/{menuId}", method = RequestMethod.GET)
    public BaseResponse<List<ResPermissions>> getMenuAuthList(@PathVariable("menuId") Long menuId) throws BaseException{
        return menuAuthService.getMenuAuthList(menuId);
    }
}
