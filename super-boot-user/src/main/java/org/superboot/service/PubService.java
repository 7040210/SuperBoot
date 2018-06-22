package org.superboot.service;

import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import com.querydsl.core.types.Predicate;

/**
 * <b> 用户中心公有接口 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
public interface PubService {


    /**
     * 获取全部组织
     * @param predicate 查询条件
     * @return
     * @throws BaseException
     */
    BaseResponse getGroupAll(Predicate predicate) throws BaseException;



    /**
     * 获取组织树
     * @return
     * @throws BaseException
     */
    BaseResponse getGroupTree(Predicate predicate)throws BaseException;


}
