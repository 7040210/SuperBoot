package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.dao.jpa.DutiesDAO;
import org.superboot.dao.jpa.EmployeesDAO;
import org.superboot.dao.jpa.EmployeesDutiesDAO;
import org.superboot.dao.jpa.OrgDAO;
import org.superboot.entity.jpa.*;
import org.superboot.entity.request.ReqEmployees;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResDuties;
import org.superboot.entity.response.ResEmployees;
import org.superboot.service.EmployeesService;

import java.util.List;

/**
 * <b> 员工基本信息实现类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class EmployeesServiceimpl extends BaseService implements EmployeesService {

    @Autowired
    private EmployeesDAO employeesDAO;

    @Autowired
    private EmployeesDutiesDAO employeesDutiesDAO;

    @Autowired
    private DutiesDAO dutiesDAO;


    @Autowired
    private OrgDAO orgDAO;


    @Override
    public BaseResponse getEmployeesList(long pkOrg, long pkGroup, Pageable pageable, Predicate predicate) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        QSuperbootEmployees qSuperbootEmployees = QSuperbootEmployees.superbootEmployees;
        QSuperbootEmployeesDuties qSuperbootEmployeesDuties = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        List<ResEmployees> list = getQueryFactory().select(Projections.bean(
                ResEmployees.class,
                qSuperbootEmployees.pkEmployees,
                qSuperbootEmployees.employeesCode,
                qSuperbootEmployees.employeesName
        )).from(qSuperbootEmployees)
                .where(
                        qSuperbootEmployees.dr.eq(BaseConstants.DATA_STATUS_OK)
                                .and(qSuperbootEmployees.pkGroup.eq(pkGroup))
                                .and(qSuperbootEmployees.pkOrganization.eq(pkOrg))
                                .and(predicate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse<ResCount> getEmployeesListCount(long pkOrg, long pkGroup, Predicate predicate) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        QSuperbootEmployees qSuperbootEmployees = QSuperbootEmployees.superbootEmployees;
        long count = getQueryFactory().select(
                qSuperbootEmployees.pkEmployees.count()
        ).from(qSuperbootEmployees)
                .where(
                        qSuperbootEmployees.dr.eq(BaseConstants.DATA_STATUS_OK)
                                .and(qSuperbootEmployees.pkGroup.eq(pkGroup))
                                .and(qSuperbootEmployees.pkOrganization.eq(pkOrg))
                                .and(predicate)
                )
                .fetchOne();
        return new BaseResponse(count);
    }

    @Override
    public BaseResponse addEmployees(ReqEmployees employees) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(employees.getPkGroup());
        //检查机构是否存在
        if (null == orgDAO.findOne(employees.getPkOrganization())) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "该机构不存在");
        }
        QSuperbootEmployees qSuperbootEmployees = QSuperbootEmployees.superbootEmployees;
        SuperbootEmployees impEmployees = getQueryFactory().selectFrom(qSuperbootEmployees).where(
                qSuperbootEmployees.pkOrganization.eq(employees.getPkOrganization())
                        .and(qSuperbootEmployees.employeesCode.eq(employees.getEmployeesCode()))
                        .and(qSuperbootEmployees.dr.eq(BaseConstants.DATA_STATUS_OK))
        ).fetchFirst();
        if (null != impEmployees) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS, "员工编号已存在");
        }
        //添加员工信息
        impEmployees = new SuperbootEmployees();
        BeanUtils.copyProperties(employees, impEmployees);
        impEmployees = employeesDAO.save(impEmployees);
        ResEmployees res = new ResEmployees();
        BeanUtils.copyProperties(impEmployees, res);
        //添加员工职务信息
        addEmployeesDuties(employees.getPkGroup(), employees.getPkDuties(), impEmployees.getPkEmployees());
        return new BaseResponse(StatusCode.ADD_SUCCESS, res);
    }

    @Override
    public BaseResponse setEmployees(ReqEmployees employees, long pkEmployees) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(employees.getPkGroup());
        SuperbootEmployees impEmployees = employeesDAO.findOne(pkEmployees);
        if (null == impEmployees) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "员工信息不存在");
        }
        QSuperbootEmployees qSuperbootEmployees = QSuperbootEmployees.superbootEmployees;
        Long existsNum = getQueryFactory().selectFrom(qSuperbootEmployees).where(
                qSuperbootEmployees.pkOrganization.eq(employees.getPkOrganization())
                        .and(qSuperbootEmployees.employeesCode.eq(employees.getEmployeesCode()))
                        .and(qSuperbootEmployees.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootEmployees.pkEmployees.notIn(pkEmployees))
        ).fetchCount();
        if (0 < existsNum) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS, "员工编号已存在");
        }
        //检查机构是否存在
        if (null == orgDAO.findOne(employees.getPkOrganization())) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "该机构不存在");
        }
        BeanUtils.copyProperties(employees, impEmployees);
        impEmployees = employeesDAO.save(impEmployees);
        ResEmployees res = new ResEmployees();
        BeanUtils.copyProperties(impEmployees, res);
        //更新员工职务
        delEmployeesDuties(pkEmployees);
        addEmployeesDuties(employees.getPkGroup(), employees.getPkDuties(), impEmployees.getPkEmployees());
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, res);
    }

    @Override
    public BaseResponse delEmployees(long pkGroup, List<Long> pkEmployees) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        //删除员工信息
        QSuperbootEmployees employees = QSuperbootEmployees.superbootEmployees;
        getQueryFactory().update(employees).set(employees.dr, BaseConstants.DATA_STATUS_DEL)
                .where(employees.pkGroup.eq(pkGroup).and(employees.pkEmployees.in(pkEmployees))).execute();
        //删除员工职务信息
        QSuperbootEmployeesDuties ed = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        getQueryFactory().update(ed).set(ed.dr, BaseConstants.DATA_STATUS_DEL).where(
                ed.dr.eq(BaseConstants.DATA_STATUS_OK).and(ed.pkEmployees.in(pkEmployees))
        ).execute();
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }


    @Override
    public BaseResponse getEmployeesDuties(long pkEmployees) throws BaseException {
        QSuperbootEmployeesDuties qSuperbootEmployeesDuties = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        QSuperbootDuties qSuperbootDuties = QSuperbootDuties.superbootDuties;
        List<ResDuties> list = getQueryFactory().select(Projections.bean(
                ResDuties.class,
                qSuperbootDuties.pkDuties,
                qSuperbootDuties.dutiesCode,
                qSuperbootDuties.dutiesName,
                qSuperbootDuties.dutiesType
        )).from(qSuperbootDuties, qSuperbootEmployeesDuties)
                .where(qSuperbootEmployeesDuties.pkDuties.eq(qSuperbootDuties.pkDuties)
                        .and(qSuperbootDuties.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootEmployeesDuties.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootEmployeesDuties.pkEmployees.eq(pkEmployees)))
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse getEmployeesDutiesList(long pkGroup) throws BaseException {
        QSuperbootDuties qSuperbootDuties = QSuperbootDuties.superbootDuties;
        List<ResDuties> list = getQueryFactory().select(Projections.bean(
                ResDuties.class,
                qSuperbootDuties.pkDuties,
                qSuperbootDuties.dutiesCode,
                qSuperbootDuties.dutiesName,
                qSuperbootDuties.dutiesType
        )).from(qSuperbootDuties)
                .where(qSuperbootDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootDuties.pkGroup.eq(pkGroup)))
                .fetch();
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse addEmployeesDuties(long pkGroup, long pkDuties, long pkEmployees) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        SuperbootDuties impDuties = dutiesDAO.findOne(pkDuties);
        if (null == impDuties || BaseConstants.DATA_STATUS_DEL == impDuties.getDr()) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "职务信息不存在");
        }
        SuperbootEmployees employees = employeesDAO.findOne(pkEmployees);
        if (null == employees || BaseConstants.DATA_STATUS_DEL == employees.getDr()) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "员工信息不存在");
        }

        SuperbootEmployeesDuties employeesDuties = employeesDutiesDAO.findByPkEmployeesAndPkDutiesAndDr(pkEmployees, pkDuties, BaseConstants.DATA_STATUS_OK);
        //判断职务是否存在
        if (null == employeesDuties ||
                BaseConstants.DATA_STATUS_OK != employeesDuties.getDr()) {
            employeesDuties = new SuperbootEmployeesDuties();
            employeesDuties.setPkDuties(pkDuties);
            employeesDuties.setPkEmployees(pkEmployees);
            employeesDutiesDAO.save(employeesDuties);
        }
        return new BaseResponse(StatusCode.ADD_SUCCESS);
    }

    @Override
    public BaseResponse delEmployeesDuties(long pkGroup, long pkEmployees, long pkDuties) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(pkGroup);
        QSuperbootEmployeesDuties qEmpDuties = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        getQueryFactory().update(qEmpDuties).set(qEmpDuties.dr, BaseConstants.DATA_STATUS_DEL).where(
                qEmpDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qEmpDuties.pkEmployees.eq(pkEmployees))
                        .and(qEmpDuties.pkDuties.eq(pkDuties))
        ).execute();
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    @Override
    public BaseResponse<ResEmployees> getEmployeesById(long employeesId) throws BaseException {
        SuperbootEmployees emp = employeesDAO.findByPkEmployeesAndDr(employeesId, BaseConstants.DATA_STATUS_OK);
        ResEmployees res = new ResEmployees();
        BeanUtils.copyProperties(emp, res);
        return new BaseResponse(res);
    }

    /**
     * 删除员工全部职务信息
     *
     * @param pkEmployees
     * @throws BaseException
     */
    private void delEmployeesDuties(long pkEmployees) throws BaseException {
        //校验操作的组织是否是自己的组织
        QSuperbootEmployeesDuties qEmpDuties = QSuperbootEmployeesDuties.superbootEmployeesDuties;
        getQueryFactory().update(qEmpDuties).set(qEmpDuties.dr, BaseConstants.DATA_STATUS_DEL).where(
                qEmpDuties.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qEmpDuties.pkEmployees.eq(pkEmployees))
        ).execute();
    }
}
