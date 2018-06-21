package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;

/**
 * <b> 日志相关接口服务 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface LogService {

    /**
     * 获取日志列表信息
     *
     * @param pageable  分页信息
     * @param predicate 查询参数
     * @return
     * @throws BaseException
     */
    BaseResponse getLogList(Pageable pageable, Predicate predicate) throws BaseException;

    /**
     * 获取日志记录数
     *
     * @return
     * @throws BaseException
     */
    BaseResponse getLogCount(Predicate predicate) throws BaseException;


    /**
     * 查询日志详细信息
     *
     * @param id
     * @return
     * @throws BaseException
     */
    BaseResponse getLogItem(String id) throws BaseException;


    /**
     * 查询统计最近一周服务调用次数
     *
     * @return
     * @throws BaseException
     */
    BaseResponse getRequestCountByWeek() throws BaseException;
}
