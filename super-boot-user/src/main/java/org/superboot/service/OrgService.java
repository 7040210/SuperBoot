package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.jpa.SuperbootOrg;
import org.superboot.entity.request.ReqOrg;
import org.superboot.entity.response.ResCount;

import java.util.List;

/**
 * <b> 机构信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface OrgService {
    /**
     * 获取机构信息表格
     *
     * @param pkGroup   组织
     * @param pageable  分页
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse getOrgList(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException;

    /**
     * 获取所有的机构列表
     *
     * @param pkGroup 组织
     * @return
     * @throws BaseException
     */
    BaseResponse getOrgCommbox(Long pkGroup) throws BaseException;

    /**
     * 获取机构信息总数
     *
     * @param pkGroup   组织
     * @param predicate 查询条件
     * @return
     */
    BaseResponse<ResCount> getOrgListCount(long pkGroup, Predicate predicate);

    /**
     * 添加机构信息
     *
     * @param org     机构信息
     * @param pkGroup 组织主键
     * @return
     * @throws BaseException
     */
    BaseResponse addOrg(ReqOrg org, long pkGroup) throws BaseException;

    /**
     * 更新机构基本信息
     *
     * @param pkGroup 组织主键
     * @param pkOrg   机构主键
     * @param org     前端提交数据
     * @return
     * @throws BaseException
     */
    BaseResponse setOrg(long pkGroup, long pkOrg, ReqOrg org) throws BaseException;

    /**
     * 更新机构基本信息
     *
     * @param org 前端提交数据
     * @return
     * @throws BaseException
     */
    BaseResponse setOrg(SuperbootOrg org) throws BaseException;

    /**
     * 删除机构基本信息
     *
     * @param pkOrg   机构主键
     * @param pkGroup 组织主键
     * @return
     * @throws BaseException
     */
    BaseResponse delOrg(List<Long> pkOrg, long pkGroup) throws BaseException;

    /**
     * 删除顶级机构
     *
     * @param pkOrg 机构主键
     * @return
     * @throws BaseException
     */
    BaseResponse delRootOrg(long pkOrg) throws BaseException;

}
