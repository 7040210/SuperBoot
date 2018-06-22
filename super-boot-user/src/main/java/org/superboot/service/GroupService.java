package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqGroup;
import org.superboot.entity.response.ResGroup;

import java.util.List;

/**
 * <b> 组织管理服务类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
public interface GroupService {

    /**
     * 添加组织
     *
     * @param group 组织信息
     * @return
     * @throws BaseException
     */
    BaseResponse addGroup(ReqGroup group) throws BaseException;


    /**
     * 修改组织信息
     * @param pkGroup
     * @param reqGroup
     * @return
     * @throws BaseException
     */
    BaseResponse setGroup(Long pkGroup, ReqGroup reqGroup) throws BaseException;

    /**
     * 删除组织信息
     * @param pkGroup
     * @return
     * @throws BaseException
     */
    BaseResponse delGroup(Long pkGroup)throws BaseException;


    /**
     * 获取组织基本信息
     * @param pageable 分页信息
     * @return
     * @throws BaseException
     */
    BaseResponse getGroupList(Pageable pageable, Predicate predicate)throws BaseException;


    /**
     * 获取组织记录总数
     * @return
     * @throws BaseException
     */
    BaseResponse getGroupCount(Predicate predicate) throws BaseException;


    /**
     * 获取组织下拉框
     *
     * @return
     */
    BaseResponse<List<ResGroup>> getGroupCombobox();
}
