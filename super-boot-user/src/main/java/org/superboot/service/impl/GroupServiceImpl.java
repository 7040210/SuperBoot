package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.dao.jpa.GroupDAO;
import org.superboot.dao.jpa.OrgDAO;
import org.superboot.entity.jpa.*;
import org.superboot.entity.request.ReqGroup;
import org.superboot.entity.request.ReqOrg;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResGroup;
import org.superboot.service.GroupService;
import org.superboot.service.OrgService;

import java.util.List;

/**
 * <b> 提供基于组织的相关操作 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class GroupServiceImpl extends BaseService implements GroupService {

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private OrgDAO orgDAO;


    @Autowired
    private Pub_Tools pubTools;

    @Autowired
    private OrgService orgService;

    @Override
    public BaseResponse addGroup(ReqGroup group) throws BaseException {
        if (null != groupDAO.findByGroupCodeAndDr(group.getGroupCode(), BaseConstants.DATA_STATUS_OK)) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS);
        }
        SuperbootGroup impGroup = new SuperbootGroup();
        BeanUtils.copyProperties(group, impGroup);
        impGroup = groupDAO.save(impGroup);

        //生成组织初始的机构节点档案
        ReqOrg org = new ReqOrg();
        org.setOrgCode(impGroup.getGroupCode());
        org.setOrgName(impGroup.getGroupName());
        org.setNodetype(0);
        org.setPkFOrg(-1L);
        org.setOrgLev(0);
        org.setOrderCode("" + pubTools.genUUID());
        org.setPlatform("Web");
        org.setVersion("v01");
        BaseResponse response = orgService.addOrg(org, impGroup.getPkGroup());
        if (BaseStatus.OK.getCode() == response.getStatus()) {
            if (StatusCode.ADD_SUCCESS.getCode() == response.getCode()) {

                ResGroup resGroup = new ResGroup();
                BeanUtils.copyProperties(impGroup, resGroup);

                return new BaseResponse(StatusCode.ADD_SUCCESS, resGroup);
            }
        }

        //如果机构生成失败，则删除已经添加的机构档案，客户端重新提交创建
        groupDAO.delete(impGroup.getPkGroup());

        return new BaseResponse(StatusCode.ADD_ERROR);

    }

    @Override
    public BaseResponse setGroup(Long pkGroup, ReqGroup reqGroup) throws BaseException {
        if (-1 != getPkGroup()) {
            checkGroupIsMy(pkGroup);
        }
        SuperbootGroup group = groupDAO.findOne(pkGroup);
        if (null == group) {
            throw new BaseException(StatusCode.DATA_NOT_FNID);
        }

        if (-1 != getPkGroup()) {
            QSuperbootOrg qSuperbootOrg = QSuperbootOrg.superbootOrg;
            SuperbootOrg org = orgDAO.findOne(qSuperbootOrg.pkGroup.eq(pkGroup).and(qSuperbootOrg.pkFOrg.eq(-1L)));
            if (null != org) {
                org.setOrgCode(reqGroup.getGroupCode());
                org.setOrgName(reqGroup.getGroupName());
                orgDAO.save(org);
            }
        }

        BeanUtils.copyProperties(reqGroup, group);
        group = groupDAO.save(group);
        //更新组织对应的机构
        SuperbootOrg org = orgDAO.findByPkOrgAndDr(group.getPkGroup(), BaseConstants.DATA_STATUS_OK);
        org.setOrgCode(group.getGroupCode());
        org.setOrgName(group.getGroupName());
        orgService.setOrg(org);

        ResGroup resGroup = new ResGroup();
        BeanUtils.copyProperties(group, resGroup);
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, resGroup);
    }

    @Override
    public BaseResponse delGroup(Long pkGroup) throws BaseException {
        if (-1 != getPkGroup()) {
            throw new BaseException(StatusCode.UNAUTHORIZED_OPERATION);
        }

        SuperbootGroup group = groupDAO.findOne(pkGroup);
        if (null == group) {
            throw new BaseException(StatusCode.DATA_NOT_FNID);
        }

        //如果系统已经有管理员则 不允许删除
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;

        if (0 < getQueryFactory()
                .select(qSuperbootUser.pkUser.count())
                .from(qSuperbootUser)
                .where(qSuperbootUser.pkGroup.eq(pkGroup)
                        .and(qSuperbootUser.dr.eq(BaseConstants.DATA_STATUS_OK)))
                .fetchOne()) {
            throw new BaseException(StatusCode.DATA_QUOTE, "组织下已存在组织管理员");
        }
        //易电务组织下没有机构
        if (-1 != group.getPkGroup()) {
            orgService.delRootOrg(group.getPkGroup());
        }


        group.setDr(BaseConstants.DATA_STATUS_DEL);
        groupDAO.save(group);
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    @Override
    public BaseResponse getGroupList(Pageable pageable, Predicate predicate) throws BaseException {
        QSuperbootGroup qSuperbootGroup = QSuperbootGroup.superbootGroup;
        List<ResGroup> list = getQueryFactory().select(Projections.bean(
                ResGroup.class,
                qSuperbootGroup.pkGroup,
                qSuperbootGroup.groupCode,
                qSuperbootGroup.groupName,
                qSuperbootGroup.groupType
        )).from(qSuperbootGroup)
                .where(qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK).and(predicate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse getGroupCount(Predicate predicate) throws BaseException {
        QSuperbootGroup qSuperbootGroup = QSuperbootGroup.superbootGroup;
        long countNum = getQueryFactory().select(qSuperbootGroup.pkGroup.count()).from(qSuperbootGroup)
                .where(qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK).and(predicate))
                .fetchOne();
        return new BaseResponse(new ResCount(countNum));
    }


    @Override
    public BaseResponse<List<ResGroup>> getGroupCombobox() {
        QSuperbootGroup qSuperbootGroup = QSuperbootGroup.superbootGroup;
        List<ResGroup> res;
        if (-1 == getPkGroup()) {
            res = getQueryFactory().select(Projections.bean(
                    ResGroup.class,
                    qSuperbootGroup.pkGroup,
                    qSuperbootGroup.groupCode,
                    qSuperbootGroup.groupName,
                    qSuperbootGroup.groupType
            )).from(qSuperbootGroup)
                    .where(
                            qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK)
                    )
                    .fetch();
        } else {
            //普华讯光组织
            long PHXG_GROUP = 400670264993513472L;
            res = getQueryFactory().select(Projections.bean(
                    ResGroup.class,
                    qSuperbootGroup.pkGroup,
                    qSuperbootGroup.groupCode,
                    qSuperbootGroup.groupName,
                    qSuperbootGroup.groupType
            )).from(qSuperbootGroup)
                    .where(
                            qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK)
                                    .and(qSuperbootGroup.pkGroup.in(getPkGroup(), PHXG_GROUP))
                    )
                    .fetch();
        }
        return new BaseResponse<>(res);
    }


}
