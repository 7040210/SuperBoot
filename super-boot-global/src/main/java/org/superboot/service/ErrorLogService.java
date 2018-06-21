package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;

/**
 * <b> 错误日志服务接口 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface ErrorLogService {

    /**
     * 按照微服务模块进行分组统计
     *
     * @return
     * @throws BaseException
     */
    BaseResponse getErrorLogGroupByAppName() throws BaseException;


    /**
     * 获取错误日志列表信息
     *
     * @param pageable  分页信息
     * @param predicate 查询参数
     * @return
     * @throws BaseException
     */
    BaseResponse getErrorLogList(Pageable pageable, Predicate predicate) throws BaseException;

    /**
     * 获取错误日志记录数
     *
     * @return
     * @throws BaseException
     */
    BaseResponse getErrorLogCount(Predicate predicate) throws BaseException;


    /**
     * 查询错误日志详细信息
     *
     * @param id
     * @return
     * @throws BaseException
     */
    BaseResponse getErrorLogItem(String id) throws BaseException;
}
