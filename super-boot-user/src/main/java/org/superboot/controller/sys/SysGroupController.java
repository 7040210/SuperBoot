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
import org.superboot.entity.jpa.SuperbootGroup;
import org.superboot.entity.request.ReqGroup;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResGroup;
import org.superboot.service.GroupService;

import java.util.List;

/**
 * <b> 组织管理 </b>
 * <p>
 * 功能描述:提供对组织信息的新增修改操作
 * </p>
 *
 */

@RequestMapping("/sys/group")
@RestController
@Api(tags = "用户中心系统组织管理接口", description = "提供对系统组织信息的维护管理")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME,BaseConstants.DEV_USER_NAME,BaseConstants.OPER_USER_NAME})
public class SysGroupController {

    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "添加组织", notes = "添加组织，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BaseResponse<ResGroup> addGroup(@RequestBody @Validated ReqGroup group) throws BaseException {
        return groupService.addGroup(group);
    }

    @ApiOperation(value = "获取组织信息", notes = "获取组织基本信息，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResGroup>> getGroupList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                     @QuerydslPredicate(root = SuperbootGroup.class) Predicate predicate) throws BaseException {
        Pageable pageable = new PageRequest(page, size);
        return groupService.getGroupList(pageable, predicate);
    }


    @ApiOperation(value = "获取组织记录总数", notes = "获取组织记录总数，系统管理员默认可以访问")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public BaseResponse<ResCount> getGroupCount(@QuerydslPredicate(root = SuperbootGroup.class) Predicate predicate) throws BaseException {
        return groupService.getGroupCount(predicate);
    }




    @ApiOperation(value = "修改组织信息", notes = "修改组织信息，系统管理员默认可以访问")
    @RequestMapping(value = "/{groupid}", method = RequestMethod.PUT)
    public BaseResponse<ResGroup> setGroup(@PathVariable("groupid") long groupId, @RequestBody @Validated ReqGroup group) throws BaseException {
        return groupService.setGroup(groupId, group);
    }


    @ApiOperation(value = "删除组织", notes = "删除组织，系统管理员默认可以访问")
    @RequestMapping(value = "/{groupid}", method = RequestMethod.DELETE)
    public BaseResponse<ResGroup> delGroup(@PathVariable("groupid") long groupId) throws BaseException {
        return groupService.delGroup(groupId);
    }

}
