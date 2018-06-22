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
import org.superboot.base.*;
import org.superboot.entity.jpa.SuperbootGroup;
import org.superboot.entity.jpa.SuperbootRole;
import org.superboot.entity.jpa.SuperbootUser;
import org.superboot.entity.request.RegisterUser;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResUser;
import org.superboot.service.UserService;

import java.util.List;

/**
 * <b> 后台用户管理 </b>
 * <p>
 * 功能描述:
 * </p>
 */

@RequestMapping("/sys/user")
@RestController
@Api(tags = "用户中心系统用户管理接口", description = "提供系统用户的管理功能")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME, BaseConstants.DEV_USER_NAME, BaseConstants.OPER_USER_NAME})
public class SysUserController {


    @Autowired
    private UserService userService;


    @ApiOperation(value = "注册管理用户", notes = "注册管理用户，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BaseResponse<BaseToken> addAdmin(@RequestBody @Validated RegisterUser regUser) throws BaseException {
        return userService.registerAdmin(regUser);
    }


    @ApiOperation(value = "锁定用户", notes = "锁定用户，系统管理员默认可以访问")
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public BaseResponse delUser(@PathVariable("userId") long userId) throws BaseException {
        return userService.delUser(userId);
    }

    @ApiOperation(value = "注册组织管理员", notes = "注册组织管理员，系统管理员默认可以访问")
    @RequestMapping(value = "/group/{group_id}", method = RequestMethod.POST)
    public BaseResponse<BaseToken> addGroupAdmin(@PathVariable("group_id") long groupId, @RequestBody @Validated RegisterUser regUser) throws BaseException {
        return userService.registerGroupAdmin(regUser, groupId);
    }


    @ApiOperation(value = "创建指定角色用户", notes = "创建指定角色用户，系统管理员默认可以访问")
    @RequestMapping(value = "/role/{role_id}", method = RequestMethod.POST)
    public BaseResponse<BaseToken> addUserByRole(@PathVariable("role_id") long rolePk, @RequestBody @Validated RegisterUser regUser) throws BaseException {
        return userService.registerRoleUser(regUser, rolePk);
    }

    @ApiOperation(value = "获取用户信息", notes = "获取系统用户信息，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResUser>> getUser(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "15") Integer size,
                                               @QuerydslPredicate(root = SuperbootUser.class) Predicate user, @QuerydslPredicate(root = SuperbootRole.class) Predicate role, @QuerydslPredicate(root = SuperbootGroup.class) Predicate group) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return userService.getUserList(pageable, false, user, role, group);
    }

    @ApiOperation(value = "获取用户信息", notes = "获取系统用户信息，系统管理员默认可以访问")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public BaseResponse<ResCount> getCount(@QuerydslPredicate(root = SuperbootUser.class) Predicate user, @QuerydslPredicate(root = SuperbootRole.class) Predicate role, @QuerydslPredicate(root = SuperbootGroup.class) Predicate group) throws BaseException {
        return userService.getUserCount(user, role, group);
    }
}
