package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.xiaoleilu.hutool.date.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.superboot.base.BaseException;
import org.superboot.base.BaseResponse;
import org.superboot.dao.mongo.DataInfoDAO;
import org.superboot.entity.mongo.DataInfo;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResLog;
import org.superboot.service.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private DataInfoDAO dataInfoDAO;


    @Autowired
    private MongoTemplate template;

    @Override
    public BaseResponse getLogList(Pageable pageable, Predicate predicate) throws BaseException {
        Page<DataInfo> page = dataInfoDAO.findAll(predicate, pageable);
        List list = new ArrayList();
        for (DataInfo dataInfo : page.getContent()) {
            ResLog resLog = new ResLog();
            BeanUtils.copyProperties(dataInfo, resLog);
            //MongoDB主键为组合主键，此处需要进行处理
            resLog.setId(dataInfo.getId().toString());
            list.add(resLog);
        }
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse getLogCount(Predicate predicate) throws BaseException {
        long count = dataInfoDAO.count(predicate);
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse getLogItem(String id) throws BaseException {
        return new BaseResponse(dataInfoDAO.findOne(id));
    }

    @Override
    public BaseResponse getRequestCountByWeek() throws BaseException {

        //查询条件信息
        Criteria operator = new Criteria();
        operator.andOperator(
                //查询小于等于今天
                Criteria.where("execDate").lte(DateUtil.today()),
                //查询大于等于7天前
                Criteria.where("execDate").gte(DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -7)))

        );

        //查询条件
        MatchOperation matchOperation = Aggregation.match(operator);


        //分组信息及返回count列命名
        GroupOperation groupOperation = Aggregation.group("appName", "execDate").count().as("count");

        //排序信息
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "execDate"));
        SortOperation sortOperation = Aggregation.sort(sort);

        //组合条件
        Aggregation aggregation = Aggregation.newAggregation(DataInfo.class, matchOperation, groupOperation, sortOperation);

        // 执行操作
        AggregationResults<Map> aggregationResults = template.aggregate(aggregation, DataInfo.class, Map.class);

        return new BaseResponse(aggregationResults.getMappedResults());
    }
}
