package org.superboot.controller.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.response.ResPermissions;
import org.superboot.service.PermissionsService;

import java.util.List;

/**
 * <b> 权限管理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@RequestMapping("/sys/permissions")
@RestController
@Api(tags = "用户中心功能权限管理接口", description = "提供对功能权限信息的维护管理")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.DEV_USER_NAME, BaseConstants.OPER_USER_NAME})
public class SysPermissionsController {

    @Autowired
    private PermissionsService permissionsService;


    @ApiOperation(value = "获取权限列表", notes = "获取权限列表，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResPermissions>> getPermissions(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "15") Integer size) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return permissionsService.getPermissions(pageable);
    }

    @ApiOperation(value = "获取全部信息", notes = "获取全部信息，系统管理员默认可以访问")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public BaseResponse<List<ResPermissions>> getAll() throws BaseException {
        return permissionsService.getAll();
    }

    @ApiOperation(value = "获取权限信息", notes = "获取权限信息，系统管理员默认可以访问")
    @RequestMapping(value = "/{permissionId}", method = RequestMethod.GET)
    public BaseResponse getPermission(@PathVariable("permissionId") long permissionId) throws BaseException {
        return permissionsService.getPermission(permissionId);
    }

    @ApiOperation(value = "删除权限信息", notes = "删除权限，系统管理员默认可以访问")
    @RequestMapping(value = "/{permissionId}", method = RequestMethod.DELETE)
    public BaseResponse delPermission(@PathVariable("permissionId") long permissionId) throws BaseException {
        return permissionsService.delPermission(permissionId);
    }
}
