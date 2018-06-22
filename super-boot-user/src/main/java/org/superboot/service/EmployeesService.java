package org.superboot.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.entity.request.ReqEmployees;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResEmployees;

import java.util.List;

/**
 * <b> 员工信息 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public interface EmployeesService {
    /**
     * 获取员工基础信息
     *
     * @param pkOrg     机构主键
     * @param pkGroup   组织主键
     * @param pageable  分页信息
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse getEmployeesList(long pkOrg, long pkGroup, Pageable pageable, Predicate predicate) throws BaseException;

    /**
     * 获取员工基础信息总数
     *
     * @param pkGroup   组织主键
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse<ResCount> getEmployeesListCount(long pkOrg, long pkGroup, Predicate predicate) throws BaseException;

    /**
     * 添加员工信息
     *
     * @param employees 员工信息
     * @return
     * @throws BaseException
     */
    BaseResponse addEmployees(ReqEmployees employees) throws BaseException;

    /**
     * 更新员工基础信息
     *
     * @param employees   员工信息
     * @param pkEmployees 员工主键
     * @return
     * @throws BaseException
     */
    BaseResponse setEmployees(ReqEmployees employees, long pkEmployees) throws BaseException;

    /**
     * 删除员工基础信息
     *
     * @param pkGroup     组织主键
     * @param pkEmployees 员工主键
     * @return
     * @throws BaseException
     */
    BaseResponse delEmployees(long pkGroup, List<Long> pkEmployees) throws BaseException;


    /**
     * 获取员工职务信息
     *
     * @param pkEmployees 员工主键
     * @return
     * @throws BaseException
     */
    BaseResponse getEmployeesDuties(long pkEmployees) throws BaseException;

    /**
     * 获取员工职务信息列表
     *
     * @param pkGroup 组织主键
     * @return
     * @throws BaseException
     */
    BaseResponse getEmployeesDutiesList(long pkGroup) throws BaseException;

    /**
     * 更新员工职务信息
     *
     * @param pkGroup     组织主键
     * @param pkDuties    职务主键
     * @param pkEmployees 雇员主键
     * @return
     * @throws BaseException
     */
    BaseResponse addEmployeesDuties(long pkGroup, long pkDuties, long pkEmployees) throws BaseException;

    /**
     * 删除员工职务
     *
     * @param pkGroup     组织主键
     * @param pkEmployees 员工主键
     * @param pkDuties    职务主键
     * @return
     * @throws BaseException
     */
    BaseResponse delEmployeesDuties(long pkGroup, long pkEmployees, long pkDuties) throws BaseException;

    /**
     * 根据主键获取员工信息
     *
     * @param employeesId
     * @return
     * @throws BaseException
     */
    BaseResponse<ResEmployees> getEmployeesById(long employeesId) throws BaseException;
}
