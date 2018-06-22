package org.superboot.controller.sys;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.mongo.ErrorInfo;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResErrLog;
import org.superboot.service.ErrorLogService;

import java.util.List;

/**
 * <b> 系统异常日志信息 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@RequestMapping("/sys/info/error")
@RestController
@Api(tags = "用户中心系统异常日志管理接口", description = "提供对异常日志进行查询功能")
@AuthRoles(roles = {BaseConstants.SYS_ADMIN_NAME,BaseConstants.DEV_USER_NAME,BaseConstants.OPER_USER_NAME})
public class SysErrLogController {


    @Autowired
    private ErrorLogService service;


    @ApiOperation(value = "获取日志列表信息", notes = "获取日志列表信息，系统管理员默认可以访问")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseResponse<List<ResErrLog>> getErrorLogList(@QuerydslPredicate(root = ErrorInfo.class) Predicate predicate, @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(value = "size", defaultValue = "15") Integer size) throws BaseException {
        //添加排序 默认按照最后访问时间进行倒排
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "execTime"));
        Pageable pageable = new PageRequest(page, size, sort);
        return service.getErrorLogList(pageable, predicate);
    }


    @ApiOperation(value = "获取日志记录数", notes = "获取日志记录数，系统管理员默认可以访问")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public BaseResponse<ResCount> getErrorLogCount(@QuerydslPredicate(root = ErrorInfo.class) Predicate predicate) throws BaseException {
        return service.getErrorLogCount(predicate);
    }

    @ApiOperation(value = "获取日志详细信息", notes = "获取日志详细信息，系统管理员默认可以访问")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BaseResponse<ErrorInfo> getErrorLogItem(@PathVariable("id") String id) throws BaseException {
        return service.getErrorLogItem(id);
    }
}
