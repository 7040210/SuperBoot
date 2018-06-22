package org.superboot.service;

import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqUserEmployees;

/**
 * <b> UserEmployeesRelaService </b>
 * <p>
 * 功能描述:用户员工关系服务接口
 * </p>
 */
public interface UserEmployeesRelaService {
    /**
     * 用户绑定员工
     *
     * @param reqUserEmployees 用户员工关系信息
     * @return
     * @throws BaseException
     */
    BaseResponse userBindEmp(ReqUserEmployees reqUserEmployees) throws BaseException;

    /**
     * 用户解绑员工
     *
     * @param pkEmployees 员工主键
     * @return
     * @throws BaseException
     */
    BaseResponse userDebindEmp(long pkEmployees) throws BaseException;

    /**
     * 获取用户员工信息
     *
     * @param pkEmployees 员工主键
     * @return
     * @throws BaseException
     */
    BaseResponse getUserEmpInfo(long pkEmployees) throws BaseException;

    /**
     * 获取组织用户列表
     *
     * @param pkGroup
     * @return
     * @throws BaseException
     */
    BaseResponse getUserList(long pkGroup) throws BaseException;
}