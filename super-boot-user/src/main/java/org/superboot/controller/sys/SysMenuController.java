package org.superboot.controller.sys;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.jpa.SuperbootMenu;
import org.superboot.entity.request.ReqMenu;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResMenu;
import org.superboot.service.MenuService;

import java.util.List;

/**
 * <b> 菜单管理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@RequestMapping("/sys/menu/conf")
@RestController
@Api(tags = "用户中心系统菜单配置接口", description = "提供对系统菜单信息的维护管理")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.DEV_USER_NAME, BaseConstants.OPER_USER_NAME})
public class SysMenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "添加菜单", notes = "添加菜单，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BaseResponse<ResMenu> addMenu(@RequestBody @Validated ReqMenu menu) throws BaseException {
        return menuService.addMenu(menu);
    }

    @ApiOperation(value = "修改菜单", notes = "修改菜单，系统管理员默认可以访问")
    @RequestMapping(value = "/{menuId}", method = RequestMethod.PUT)
    public BaseResponse<ResMenu> setMenu(@PathVariable("menuId") long menuId, @RequestBody @Validated ReqMenu menu) throws BaseException {
        return menuService.setMenu(menuId, menu);
    }


    @ApiOperation(value = "删除菜单", notes = "删除菜单，系统管理员默认可以访问")
    @RequestMapping(value = "/{menuId}", method = RequestMethod.DELETE)
    public BaseResponse delMenu(@PathVariable("menuId") long menuId) throws BaseException {
        return menuService.delMenu(menuId);
    }

    @ApiOperation(value = "获取菜单信息", notes = "获取菜单信息，系统管理员默认可以访问")
    @RequestMapping(value = "/{menuId}", method = RequestMethod.GET)
    public BaseResponse<ResMenu> getMenu(@PathVariable("menuId") long menuId) throws BaseException {
        return menuService.getMenu(menuId);
    }

    @ApiOperation(value = "获取全部菜单", notes = "获取全部菜单，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResMenu>> getItems(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                @QuerydslPredicate(root = SuperbootMenu.class) Predicate predicate) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return menuService.getMenus(-1, pageable, predicate);
    }

    @ApiOperation(value = "获取角色未授权菜单", notes = "获取角色未授权菜单，系统管理员默认可以访问")
    @RequestMapping(value = "/getRoleNoMenu/{role_id}", method = RequestMethod.GET)
    public BaseResponse<List<ResMenu>> getRoleNoMenu(@PathVariable("role_id") Long pkRole,
                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                     @QuerydslPredicate(root = SuperbootMenu.class) Predicate predicate) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return menuService.getRoleNoMenu(pkRole, -1L, pageable, predicate);
    }

    @ApiOperation(value = "获取角色菜单", notes = "获取角色菜单，系统管理员默认可以访问")
    @GetMapping(value = "/getItemsByRole/{role_id}")
    public BaseResponse<List<ResMenu>> getItemsByRole(@PathVariable("role_id") Long pkRole) throws BaseException {
        return menuService.getMenusByRole(pkRole);
    }

    @ApiOperation(value = "获取菜单总数", notes = "获取菜单总数，系统管理员默认可以访问")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public BaseResponse<ResCount> getCount(@QuerydslPredicate(root = SuperbootMenu.class) Predicate predicate) throws BaseException {
        return menuService.getCount(-1, predicate);
    }


}
