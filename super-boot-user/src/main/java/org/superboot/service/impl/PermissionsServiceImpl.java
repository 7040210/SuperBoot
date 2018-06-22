package org.superboot.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.dao.jpa.PermissionsDAO;
import org.superboot.dao.jpa.PermissionsResourceDAO;
import org.superboot.entity.jpa.QSuperbootPermissions;
import org.superboot.entity.jpa.SuperbootPermissions;
import org.superboot.entity.jpa.SuperbootPermissionsResource;
import org.superboot.service.PermissionsService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <b> 资源权限服务管理类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class PermissionsServiceImpl extends BaseService implements PermissionsService {

    @Autowired
    private PermissionsDAO permissionsDAO;

    @Autowired
    private PermissionsResourceDAO permissionsResourceDAO;

    @Override
    public BaseResponse getAll() throws BaseException {
        QSuperbootPermissions qSuperbootPermissions = QSuperbootPermissions.superbootPermissions;
        List list = getQueryFactory().
                selectFrom(qSuperbootPermissions)
                .where(qSuperbootPermissions.pkGroup.in(-1).and(qSuperbootPermissions.dr.eq(BaseConstants.DATA_STATUS_OK)))
                .orderBy(qSuperbootPermissions.permissionsCode.asc())
                .fetch()
                .stream()
                .map(tuple -> {
                    return getPermissionsMap(tuple);
                }).collect(Collectors.toList());
        return new BaseResponse(JSON.toJSON(list));
    }

    @Override
    public BaseResponse getPermissions(Pageable pageable) throws BaseException {
        QSuperbootPermissions qSuperbootPermissions = QSuperbootPermissions.superbootPermissions;
        List list = getQueryFactory().
                selectFrom(qSuperbootPermissions)
                .where(qSuperbootPermissions.pkGroup.in(-1).and(qSuperbootPermissions.dr.eq(BaseConstants.DATA_STATUS_OK)))
                .orderBy(qSuperbootPermissions.permissionsCode.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    return getPermissionsMap(tuple);
                }).collect(Collectors.toList());
        return new BaseResponse(JSON.toJSON(list));
    }

    @Override
    public BaseResponse getPermission(long permissionId) throws BaseException {
        return new BaseResponse(permissionsDAO.findOne(permissionId));
    }

    @Override
    public BaseResponse delPermission(long permissionId) throws BaseException {

        SuperbootPermissions impPermissions = permissionsDAO.findOne(permissionId);
        if (null != impPermissions) {
            //删除权限资源信息
            List<SuperbootPermissionsResource> list = permissionsResourceDAO.findByPkPermissionsAndDr(permissionId, BaseConstants.DATA_STATUS_OK);
            for (SuperbootPermissionsResource resource : list) {
                resource.setDr(BaseConstants.DATA_STATUS_DEL);
            }
            permissionsResourceDAO.save(list);
            //删除权限信息
            impPermissions.setDr(BaseConstants.DATA_STATUS_DEL);
            permissionsDAO.save(impPermissions);
        }
        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }

    /**
     * 用于构造前端返回数据，后期需要改造为实体类，此处只是为了实现写法
     *
     * @param tuple
     * @return
     */
    public Map<String, Object> getPermissionsMap(SuperbootPermissions tuple) {
        QSuperbootPermissions qSuperbootPermissions = QSuperbootPermissions.superbootPermissions;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(qSuperbootPermissions.permissionsCode.getMetadata().getName(), tuple.getPermissionsCode());
        map.put(qSuperbootPermissions.permissionsName.getMetadata().getName(), tuple.getPermissionsName());
        map.put(qSuperbootPermissions.permissionsInfo.getMetadata().getName(), tuple.getPermissionsInfo());
        map.put(qSuperbootPermissions.pkPermissions.getMetadata().getName(), tuple.getPkPermissions());
        return map;
    }
}
