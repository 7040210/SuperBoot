package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.dao.jpa.EmployeesDAO;
import org.superboot.dao.jpa.OrgDAO;
import org.superboot.dao.mybatis.PubMapper;
import org.superboot.entity.jpa.QSuperbootEmployees;
import org.superboot.entity.jpa.QSuperbootOrg;
import org.superboot.entity.jpa.SuperbootOrg;
import org.superboot.entity.request.ReqOrg;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResOrg;
import org.superboot.service.OrgService;

import java.util.ArrayList;
import java.util.List;

/**
 * <b> 机构基础信息实现类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class OrgServiceImpl extends BaseService implements OrgService {

    @Autowired
    private OrgDAO orgDAO;


    @Autowired
    private EmployeesDAO empDao;

    @Autowired
    private Pub_Tools pubTools;

    @Autowired
    private PubMapper pubMapper;

    @Override
    public BaseResponse getOrgList(long pkGroup, Pageable pageable, Predicate predicate) throws BaseException {
        QSuperbootOrg qSuperbootOrg = QSuperbootOrg.superbootOrg;
        List<ResOrg> list = getQueryFactory().select(Projections.bean(
                ResOrg.class,
                qSuperbootOrg.pkOrg,
                qSuperbootOrg.orgCode,
                qSuperbootOrg.orgName,
                qSuperbootOrg.nodetype,
                qSuperbootOrg.orgLev,
                qSuperbootOrg.pkFOrg,
                qSuperbootOrg.isEnd
        )).from(qSuperbootOrg)
                .where(qSuperbootOrg.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootOrg.pkGroup.eq(pkGroup))
                        .and(predicate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse getOrgCommbox(Long pkGroup) throws BaseException {
        QSuperbootOrg qSuperbootOrg = QSuperbootOrg.superbootOrg;
        List<ResOrg> list = getQueryFactory().select(Projections.bean(
                ResOrg.class,
                qSuperbootOrg.pkOrg,
                qSuperbootOrg.orgCode,
                qSuperbootOrg.orgName,
                qSuperbootOrg.nodetype,
                qSuperbootOrg.orgLev,
                qSuperbootOrg.pkFOrg,
                qSuperbootOrg.isEnd
        )).from(qSuperbootOrg)
                .where(qSuperbootOrg.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootOrg.pkGroup.eq(pkGroup).and(qSuperbootOrg.pkOrg.ne(pkGroup)))
                )
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse<ResCount> getOrgListCount(long pkGroup, Predicate predicate) {
        QSuperbootOrg qSuperbootOrg = QSuperbootOrg.superbootOrg;
        long count = getQueryFactory().select(qSuperbootOrg.pkOrg.count()).from(qSuperbootOrg)
                .where(qSuperbootOrg.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootOrg.pkGroup.eq(pkGroup))
                        .and(predicate)
                )
                .fetchOne();
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse addOrg(ReqOrg org, long pkGroup) throws BaseException {
        //校验操作的组织是否是自己的组织
        if (-1 != getPkGroup()) {
            checkGroupIsMy(pkGroup);
        }
        //判断机构编号是否重复
        if (null != orgDAO.findByPkGroupAndOrgCodeAndDr(pkGroup, org.getOrgCode(), BaseConstants.DATA_STATUS_OK)) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS);
        }
        //判断上级是否存在
        if (null != org.getPkFOrg() && -1 != org.getPkFOrg()) {
            SuperbootOrg fOrg = orgDAO.findByPkOrgAndDr(org.getPkFOrg(), BaseConstants.DATA_STATUS_OK);
            if (null == fOrg) {
                throw new BaseException(StatusCode.DATA_NOT_FNID, "上级机构不存在");
            }
            //设置机构级别,上级机构级别+1
            if (null == org.getOrgLev()) {
                org.setOrgLev(fOrg.getOrgLev() + 1);
            }

            //设置机构序号
            if (null == org.getOrderCode()) {
                org.setOrderCode(pubTools.genUUID() + "");
            }
        }

        SuperbootOrg impOrg = new SuperbootOrg();
        BeanUtils.copyProperties(org, impOrg);
        //设置机构为末级
        impOrg.setIsEnd(BaseConstants.TRUE);
        //设置机构组织
        impOrg.setPkGroup(pkGroup);
        //如果是用户中心提交的初始化档案则自动赋值主键
        if (null != org.getPkFOrg() && -1 == org.getPkFOrg()) {
            impOrg.setPkGroup(pkGroup);
            impOrg.setPkOrg(pkGroup);
        } else {
            //更新父级机构
            if (null == impOrg.getPkFOrg()) {
                impOrg.setPkFOrg(pkGroup);
            }
            updateForg(impOrg);
        }

        ResOrg res = new ResOrg();
        BeanUtils.copyProperties(orgDAO.save(impOrg), res);
        return new BaseResponse(StatusCode.ADD_SUCCESS, res);
    }

    @Override
    public BaseResponse setOrg(long pkGroup, long pkOrg, ReqOrg org) throws BaseException {
        //校验操作的组织是否是自己的组织
        if (-1 != getPkGroup()) {
            checkGroupIsMy(pkGroup);
        }
        SuperbootOrg impOrg = orgDAO.findOne(pkOrg);
        if (null != impOrg) {
            org.setOrderCode(impOrg.getOrderCode());
            //更新父级机构
            updateForg(impOrg);
            BeanUtils.copyProperties(org, impOrg);
            impOrg = orgDAO.save(impOrg);
        }
        ResOrg res = new ResOrg();
        BeanUtils.copyProperties(impOrg, res);
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, res);
    }

    @Override
    public BaseResponse setOrg(SuperbootOrg org) throws BaseException {
        ReqOrg req = new ReqOrg();
        BeanUtils.copyProperties(org, req);
        return setOrg(org.getPkGroup(), org.getPkOrg(), req);
    }

    @Override
    public BaseResponse delOrg(List<Long> pkOrg, long pkGroup) throws BaseException {
        //校验操作的组织是否是自己的组织
        if (-1 != getPkGroup()) {
            checkGroupIsMy(pkGroup);
        }
        QSuperbootOrg org = QSuperbootOrg.superbootOrg;
        long count = orgDAO.count(org.pkFOrg.in(pkOrg).and(org.pkGroup.eq(pkGroup)).and(org.dr.eq(BaseConstants.DATA_STATUS_OK)));
        if (0 < count) {
            throw new BaseException(StatusCode.DATA_QUOTE, "存在下级机构，无法删除");
        }
        //检查机构下是否有员工
        QSuperbootEmployees qEmp = QSuperbootEmployees.superbootEmployees;
        long empCount = empDao.count(qEmp.dr.eq(BaseConstants.DATA_STATUS_OK).and(qEmp.pkOrganization.in(pkOrg)));
        if (0 < empCount) {
            throw new BaseException(StatusCode.DATA_QUOTE, "机构下存在员工，无法删除");
        }

        //删除机构基本信息
        getQueryFactory().update(org).set(org.dr, BaseConstants.DATA_STATUS_DEL)
                .where(org.pkGroup.eq(pkGroup).and(org.pkOrg.in(pkOrg))
                ).execute();
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    @Override
    public BaseResponse delRootOrg(long pkOrg) throws BaseException {
        List orgList = new ArrayList();
        orgList.add(pkOrg);
        return delOrg(orgList, pkOrg);
    }


    /**
     * 更新父级机构
     *
     * @param impOrg
     * @throws BaseException
     */
    private void updateForg(SuperbootOrg impOrg) throws BaseException {
        SuperbootOrg forg = orgDAO.findOne(impOrg.getPkFOrg());
        if (null != forg) {
            forg.setIsEnd(BaseConstants.FALSE);
            forg = orgDAO.save(forg);
            impOrg.setPkGroup(forg.getPkGroup());
            impOrg.setIsEnd(BaseConstants.FALSE);
            impOrg.setOrgLev(forg.getOrgLev() + 1);
            impOrg.setOrderCode(pubTools.genUUID() + "");
        }

    }
}
