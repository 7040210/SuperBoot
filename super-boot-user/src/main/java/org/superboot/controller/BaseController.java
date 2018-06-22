package org.superboot.controller;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.jpa.SuperbootGroup;
import org.superboot.entity.response.ResGroup;
import org.superboot.entity.response.ResTree;
import org.superboot.service.PubService;

import java.util.List;

/**
 * <b> 用户中心公有接口 </b>
 * <p>
 * 功能描述:提供一些公共接口
 * </p>
 */
@RequestMapping("/base")
@RestController
@Api(tags = "用户中心公有接口", description = "用户中心公有接口")
@AuthRoles(roles = {BaseConstants.ALL_USER_NAME})
public class BaseController {

    @Autowired
    private PubService pubService;


    @ApiOperation(value = "获取全部组织信息", notes = "获取全部组织信息,主要用于下拉使用")
    @RequestMapping(value = "/group/all", method = RequestMethod.GET)
    public BaseResponse<List<ResGroup>> getGroupAll(@QuerydslPredicate(root = SuperbootGroup.class) Predicate predicate) throws BaseException {
        return pubService.getGroupAll(predicate);
    }

    @ApiOperation(value = "获取组织树", notes = "获取组织树,主要用于构造菜单使用")
    @RequestMapping(value = "/group/tree", method = RequestMethod.GET)
    public BaseResponse<List<ResTree>> getGroupTree(@QuerydslPredicate(root = SuperbootGroup.class) Predicate predicate) throws BaseException {
        return pubService.getGroupTree(predicate);
    }
}
