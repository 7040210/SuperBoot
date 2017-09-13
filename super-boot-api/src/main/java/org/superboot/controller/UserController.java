package org.superboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.BaseRequest;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootException;
import org.superboot.entity.base.api.RegisterUser;
import org.superboot.entity.base.sys.SysUser;
import org.superboot.repository.sql.SysUserRepository;
import org.superboot.service.AuthService;

import java.util.List;

/**
 * <b> 用户管理 </b>
 * <p>
 * 功能描述:提供用户基本操作，CRUD操作
 * </p>
 *
 * @author jesion
 * @date 2017/9/7
 * @time 21:32
 * @Path org.superboot.controller.UserController
 */
@RequestMapping("/users")
@Api(tags = "用户管理", description = "提供用户信息维护功能")
@RestController
public class UserController {

    @Autowired
    private SysUserRepository userReository;


    @Autowired
    private AuthService authService;

    /**
     * 添加管理员，要求用户必须是管理员才可以添加
     *
     * @param regUser
     * @return
     * @throws AuthenticationException
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "注册管理用户", notes = "注册管理用户，只有管理员才可以访问")
    @RequestMapping(value = "/register_admin", method = RequestMethod.POST)
    public BaseResponse addAdmin(@RequestBody RegisterUser regUser) throws SuperBootException {
        return authService.register_admin(regUser);
    }

    @ApiOperation(value = "注册普通用户", notes = "注册普通用户")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponse addUser(@RequestBody RegisterUser regUser) throws SuperBootException {
        return authService.register(regUser);
    }


    @ApiOperation(value = "获取全部用户信息", notes = "获取全部用户信息，用户只有是系统管理员才可以访问")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse getAllUsers(@ModelAttribute BaseRequest request) throws SuperBootException {
        List<SysUser> user = userReository.findAll();
        return new BaseResponse(user);
    }

    @ApiOperation(value = "获取单个用户", notes = "获取单个用户，用户只有是系统管理员才可以访问")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BaseResponse getUser(@PathVariable Long id, @ModelAttribute BaseRequest request) throws SuperBootException {
        return new BaseResponse(userReository.findOne(id));
    }

}
