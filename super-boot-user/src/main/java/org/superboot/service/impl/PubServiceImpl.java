package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.base.BaseService;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.entity.jpa.QSuperbootGroup;
import org.superboot.entity.response.ResGroup;
import org.superboot.entity.response.ResTree;
import org.superboot.service.PubService;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class PubServiceImpl extends BaseService implements PubService {

    @Autowired
    private Pub_RedisUtils redisUtils;

    @Override
    public BaseResponse getGroupAll(Predicate predicate) throws BaseException {

        return new BaseResponse(getGroupList(predicate));
    }

    @Override
    public BaseResponse getGroupTree(Predicate predicate) throws BaseException {
        //获取组织信息
        List<ResGroup> groupList = getGroupList(predicate);
        List<ResTree> node = new ArrayList<ResTree>();
        ResTree tree = new ResTree();
        tree.setId(0L);
        tree.setMenuCode("0");
        tree.setMenuName("组织信息");
        tree.setPkGroup(-1L);
        for (ResGroup group : groupList) {
            ResTree treeItem = new ResTree();
            treeItem.setId(group.getPkGroup());
            treeItem.setMenuCode(group.getGroupCode());
            treeItem.setMenuName(group.getGroupName());
            treeItem.setPkGroup(group.getPkGroup());
            if (null == tree.getChildren()) {
                List<ResTree> children = new ArrayList<ResTree>();
                children.add(treeItem);
                tree.setChildren(children);
            } else {
                tree.getChildren().add(treeItem);
            }

        }

        node.add(tree);

        return new BaseResponse(node);
    }


    /**
     * 获取组织信息
     *
     * @param predicate 过滤条件
     * @return
     */
    private List<ResGroup> getGroupList(Predicate predicate) {
        QSuperbootGroup qSuperbootGroup = QSuperbootGroup.superbootGroup;
        List<ResGroup> list = null;
        if (-1 == getPkGroup()) {
            list = getQueryFactory().select(Projections.bean(
                    ResGroup.class,
                    qSuperbootGroup.pkGroup,
                    qSuperbootGroup.groupCode,
                    qSuperbootGroup.groupName,
                    qSuperbootGroup.groupType
            )).from(qSuperbootGroup)
                    .where(qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK).and(predicate))
                    .fetch();
        } else {
            list = getQueryFactory().select(Projections.bean(
                    ResGroup.class,
                    qSuperbootGroup.pkGroup,
                    qSuperbootGroup.groupCode,
                    qSuperbootGroup.groupName,
                    qSuperbootGroup.groupType
            )).from(qSuperbootGroup)
                    .where(qSuperbootGroup.dr.eq(BaseConstants.DATA_STATUS_OK).and(qSuperbootGroup.pkGroup.eq(getPkGroup())).and(predicate))
                    .fetch();
        }

        return list;
    }
}
