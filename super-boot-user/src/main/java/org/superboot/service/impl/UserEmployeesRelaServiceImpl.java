package org.superboot.service.impl;

import com.querydsl.core.types.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.dao.jpa.EmployeesDAO;
import org.superboot.dao.jpa.UserDAO;
import org.superboot.dao.jpa.UserEmployeesDAO;
import org.superboot.entity.jpa.*;
import org.superboot.entity.request.ReqUserEmployees;
import org.superboot.entity.response.ResUser;
import org.superboot.entity.response.ResUserEmpInfo;
import org.superboot.service.UserEmployeesRelaService;

import java.util.List;

/**
 * <b> UserEmployeesRelaServiceSuperbootl </b>
 * <p>
 * 功能描述:用户员工关系服务实现体
 * </p>
 */
@Service
public class UserEmployeesRelaServiceImpl extends BaseService implements UserEmployeesRelaService {
    @Autowired
    private UserEmployeesDAO userEmpDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmployeesDAO empDAO;

    @Override
    public BaseResponse userBindEmp(ReqUserEmployees reqUserEmployees) throws BaseException {
        SuperbootEmployees emp = empDAO.findByPkEmployeesAndDr(reqUserEmployees.getPkEmployees(), BaseConstants.DATA_STATUS_OK);
        if (null == emp) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "员工信息不存在");
        }
        SuperbootUser user = userDAO.findByPkUserAndDr(reqUserEmployees.getPkUser(), BaseConstants.DATA_STATUS_OK);
        if (null == user) {
            throw new BaseException(StatusCode.DATA_NOT_FNID, "用户信息不存在");
        }
        SuperbootUserEmployees userEmp = userEmpDAO.findByPkEmployeesAndDr(reqUserEmployees.getPkEmployees(), BaseConstants.DATA_STATUS_OK);
        if (null != userEmp) {
            throw new BaseException(StatusCode.DATA_QUOTE, "员工已绑定其他用户,请先进行解绑");
        }
        //删除用户绑定所有员工
        QSuperbootUserEmployees qUserEmp = QSuperbootUserEmployees.superbootUserEmployees;
        getQueryFactory().update(qUserEmp).set(qUserEmp.dr, BaseConstants.DATA_STATUS_DEL).where(
                qUserEmp.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qUserEmp.pkUser.eq(reqUserEmployees.getPkUser()))
        ).execute();
        //保存员工雇员关系
        userEmp = new SuperbootUserEmployees();
        BeanUtils.copyProperties(reqUserEmployees, userEmp);
        userEmp = userEmpDAO.save(userEmp);
        //封装响应信息
        ResUserEmpInfo res = new ResUserEmpInfo();
        BeanUtils.copyProperties(userEmp, res);
        BeanUtils.copyProperties(user, res);
        BeanUtils.copyProperties(emp, res);
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, "绑定成功");
    }

    @Override
    public BaseResponse userDebindEmp(long pkEmployees) throws BaseException {
        QSuperbootUserEmployees qUserEmp = QSuperbootUserEmployees.superbootUserEmployees;
        getQueryFactory().update(qUserEmp).set(qUserEmp.dr, BaseConstants.DATA_STATUS_DEL).where(
                qUserEmp.dr.eq(BaseConstants.DATA_STATUS_OK)
                        .and(qUserEmp.pkEmployees.eq(pkEmployees))
        ).execute();
        return new BaseResponse(StatusCode.UPDATE_SUCCESS, "解绑成功");
    }

    @Override
    public BaseResponse getUserEmpInfo(long pkEmployees) throws BaseException {
        SuperbootUserEmployees userEmp = userEmpDAO.findByPkEmployeesAndDr(pkEmployees, BaseConstants.DATA_STATUS_OK);
        ResUserEmpInfo res = new ResUserEmpInfo();
        if (null != userEmp) {
            BeanUtils.copyProperties(userEmp, res);
            //获取用户信息
            SuperbootUser user = userDAO.findByPkUserAndDr(userEmp.getPkUser(), BaseConstants.DATA_STATUS_OK);
            BeanUtils.copyProperties(user, res);
            //获取员工信息
            SuperbootEmployees emp = empDAO.findByPkEmployeesAndDr(userEmp.getPkEmployees(), BaseConstants.DATA_STATUS_OK);
            BeanUtils.copyProperties(emp, res);
        }
        return new BaseResponse(res);
    }

    @Override
    public BaseResponse getUserList(long pkGroup) throws BaseException {
        QSuperbootUser qUser = QSuperbootUser.superbootUser;
        List list = getQueryFactory().select(Projections.bean(
                ResUser.class,
                qUser.pkUser,
                qUser.userName,
                qUser.userCode
        )).from(qUser).where(
                qUser.pkGroup.eq(pkGroup)
                        .and(qUser.dr.eq(BaseConstants.DATA_STATUS_OK)
                                .and(qUser.userStatus.eq(0)))
        ).fetch();
        return new BaseResponse(list);
    }
}