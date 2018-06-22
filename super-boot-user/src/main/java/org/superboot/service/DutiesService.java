package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqDuties;
import org.superboot.entity.response.ResCount;

import java.util.List;

/**
 * <b> 职务信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface DutiesService {

    /**
     * 添加预制职务信息
     *
     * @param duties 职务信息
     * @return
     * @throws BaseException
     */
    BaseResponse addDuties(ReqDuties duties) throws BaseException;

    /**
     * 添加自建职务信息
     *
     * @param duties 职务信息
     * @return
     * @throws BaseException
     */
    BaseResponse addDuties(ReqDuties duties, long pkGroup) throws BaseException;

    /**
     * 获取职务信息
     *
     * @param pkGroup   组织主键
     * @param pageable  分页信息
     * @param predicate 查询条件
     * @return
     */
    BaseResponse getDuties(long pkGroup, Pageable pageable, Predicate predicate);

    /**
     * 获取职务信息总数
     *
     * @param pkGroup
     * @param predicate
     * @return
     */
    BaseResponse<ResCount> getDutiesCount(long pkGroup, Predicate predicate);

    /**
     * 更新职务信息
     *
     * @param pkGroup   组织主键
     * @param pkDuties  职务主键
     * @param reqDuties 提交职务信息
     * @return
     */
    BaseResponse setDuties(long pkGroup, long pkDuties, ReqDuties reqDuties) throws BaseException;

    /**
     * 删除职务信息
     *
     * @param pkGroup  组织主键
     * @param pkDuties 职务主键
     * @return
     */
    BaseResponse delDuties(long pkGroup, List<Long> pkDuties) throws BaseException;

}
