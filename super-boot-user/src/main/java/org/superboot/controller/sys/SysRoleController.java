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
import org.superboot.entity.jpa.SuperbootRole;
import org.superboot.entity.request.ReqRole;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResRole;
import org.superboot.service.RoleService;

import java.util.List;

/**
 * <b> 系统角色管理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@RequestMapping("/sys/role/conf")
@RestController
@Api(tags = "用户中心系统角色配置接口", description = "提供对系统角色信息的维护管理")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.DEV_USER_NAME, BaseConstants.OPER_USER_NAME})
public class SysRoleController {


    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "添加系统角色", notes = "添加系统角色，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BaseResponse<ResRole> addSysRole(@RequestBody @Validated ReqRole role) throws BaseException {
        return roleService.addRole(-1, role);
    }


    @ApiOperation(value = "删除系统角色", notes = "删除角色，系统管理员默认可以访问")
    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    public BaseResponse delSysRole(@PathVariable("roleId") long roleId) throws BaseException {
        return roleService.delRole(roleId);
    }

    @ApiOperation(value = "修改系统角色", notes = "修改系统角色，系统管理员默认可以访问")
    @RequestMapping(value = "/{roleId}", method = RequestMethod.PUT)
    public BaseResponse setSysRole(@PathVariable("roleId") long roleId, @RequestBody @Validated ReqRole role) throws BaseException {
        return roleService.setRole(roleId, role);
    }


    @ApiOperation(value = "获取全部角色，分页查询", notes = "获取全部角色，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResRole>> getItems(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                @QuerydslPredicate(root = SuperbootRole.class) Predicate predicate) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return roleService.getRoles(-1, pageable, predicate);
    }

    @ApiOperation(value = "获取记录总数", notes = "获取全部角色，系统管理员默认可以访问")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public BaseResponse<ResCount> getCount(@QuerydslPredicate(root = SuperbootRole.class) Predicate predicate) throws BaseException {
        return roleService.getCount(-1, predicate);
    }

    @ApiOperation(value = "获取全部角色", notes = "获取全部角色多用于下拉，系统管理员默认可以访问")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public BaseResponse<List<ResRole>> getAll(@QuerydslPredicate(root = SuperbootRole.class) Predicate predicate) throws BaseException {
        return roleService.getRoleAll(-1, predicate);
    }
}
