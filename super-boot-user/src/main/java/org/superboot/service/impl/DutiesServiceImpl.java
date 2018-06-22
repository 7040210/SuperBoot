package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.dao.jpa.DutiesDAO;
import org.superboot.entity.jpa.QSuperbootDuties;
import org.superboot.entity.jpa.QSuperbootEmployees;
import org.superboot.entity.jpa.QSuperbootEmployeesDuties;
import org.superboot.entity.jpa.SuperbootDuties;
import org.superboot.entity.request.ReqDuties;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResDuties;
import org.superboot.service.DutiesService;

import java.util.List;

/**
 * <b> 职务基本信息实现类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class DutiesServiceImpl extends BaseService implements DutiesService {

    @Autowired
    private DutiesDAO dutiesDAO;

    @Override
    public BaseResponse addDuties(ReqDuties duties) throws BaseException {
        QSuperbootDuties qSuperbootDuties = QSuperbootDuties.superbootDuties;

        SuperbootDuties impDuties = dutiesDAO.findOne(qSuperbootDuties.pkGroup.isNull()
                .and(qSuperbootDuties.dr.eq(BaseConstants.DATA_STATUS_OK))
                .and(qSuperbootDuties.dutiesCode.eq(duties.getDutiesCode())));

        if (null != impDuties) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS, "职务编号不能重复");
        }
        impDuties = new SuperbootDuties();
        BeanUtils.copyProperties(duties, impDuties);
        impDuties.setDutiesType(0);
        impDuties = dutiesDAO.save(impDuties);

        ResDuties resDuties = new ResDuties();
        BeanUtils.copyProperties(impDuties, resDuties);

        return new BaseResponse(StatusCode.ADD_SUCCESS, resDuties);
    }

    @Override
    public BaseResponse addDuties(ReqDuties duties, long pkGroup) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        QSuperbootDuties qSuperbootDuties = QSuperbootDuties.superbootDuties;

        SuperbootDuties impDuties = dutiesDAO.findOne(qSuperbootDuties.pkGroup.eq(pkGroup)
                .and(qSuperbootDuties.dr.eq(BaseConstants.DATA_STATUS_OK))
                .and(qSuperbootDuties.dutiesCode.eq(duties.getDutiesCode())));

        if (null != impDuties) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS, "职务编码已存在");
        }
        impDuties = new SuperbootDuties();
        BeanUtils.copyProperties(duties, impDuties);
        impDuties.setDutiesType(1);
        impDuties.setPkGroup(pkGroup);
        impDuties = dutiesDAO.save(impDuties);
        ResDuties resDuties = new ResDuties();
        BeanUtils.copyProperties(impDuties, resDuties);
        return new BaseResponse(StatusCode.ADD_SUCCESS, resDuties);
    }

    @Override
    public BaseResponse getDuties(long pkGroup, Pageable pageable, Predicate predicate) {
        QSuperbootDuties impDuties = QSuperbootDuties.superbootDuties;
        List<ResDuties> list = getQueryFactory().select(Projections.bean(
                ResDuties.class,
                impDuties.pkDuties,
                impDuties.dutiesType,
                impDuties.dutiesName,
                impDuties.dutiesCode
        )).from(impDuties).where(
                impDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(impDuties.pkGroup.eq(pkGroup).and(predicate))
        ).offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse<ResCount> getDutiesCount(long pkGroup, Predicate predicate) {
        QSuperbootDuties impDuties = QSuperbootDuties.superbootDuties;
        long count = getQueryFactory().select(
                impDuties.pkDuties.count()
        ).from(impDuties)
                .where(
                        impDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                                .and(impDuties.pkGroup.eq(pkGroup).and(predicate)
                                )
                ).fetchOne();
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse setDuties(long pkGroup, long pkDuties, ReqDuties reqDuties) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        SuperbootDuties duties = dutiesDAO.findOne(pkDuties);
        if (null == duties) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "职务信息不存在");
        }
        QSuperbootDuties qSuperbootDuties = QSuperbootDuties.superbootDuties;
        long existsNum = getQueryFactory().selectOne().from(qSuperbootDuties).where(
                qSuperbootDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootDuties.dutiesCode.eq(reqDuties.getDutiesCode()))
                        .and(qSuperbootDuties.pkDuties.notIn(pkDuties))
        ).fetchCount();
        if (0 < existsNum) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS, "职务编码已存在");
        }
        ResDuties res = new ResDuties();
        BeanUtils.copyProperties(reqDuties, duties);
        duties = dutiesDAO.save(duties);
        BeanUtils.copyProperties(duties, res);
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, res);
    }

    @Override
    public BaseResponse delDuties(long pkGroup, List<Long> pkDuties) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        QSuperbootEmployees qEmp = QSuperbootEmployees.superbootEmployees;
        QSuperbootEmployeesDuties qEmpDuties = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        long existsNum = getQueryFactory().selectOne().from(qEmp, qEmpDuties).where(
                qEmp.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qEmp.pkEmployees.eq(qEmpDuties.pkEmployees))
                        .and(qEmpDuties.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qEmpDuties.pkDuties.in(pkDuties))
        ).fetchCount();
        if (0 < existsNum) {
            throw new BaseException(StatusCode.DELETE_ERROR, "职务已绑定员工");
        }
        QSuperbootDuties duties = QSuperbootDuties.superbootDuties;
        getQueryFactory().update(duties).set(duties.dr, BaseConstants.DATA_STATUS_DEL)
                .where(duties.pkGroup.eq(pkGroup)
                        .and(duties.pkDuties.in(pkDuties))).execute();
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }
}
