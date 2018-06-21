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
import org.superboot.dao.mongo.ErrinfoDAO;
import org.superboot.entity.mongo.ErrorInfo;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResErrLog;
import org.superboot.service.ErrorLogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    private ErrinfoDAO errinfoDAO;

    @Autowired
    private MongoTemplate template;

    @Override
    public BaseResponse getErrorLogGroupByAppName() throws BaseException {

        //查询条件信息
        Criteria operator = new Criteria();
        operator.andOperator(
                //查询当天的数据汇总
                Criteria.where("execDate").is(DateUtil.today())
        );

        //查询条件
        MatchOperation matchOperation = Aggregation.match(operator);


        //分组信息及返回count列命名
        GroupOperation groupOperation = Aggregation.group("serUrl", "serPort", "appName", "appSer", "reqUrl", "execDate").count().as("count");

        //排序信息
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "count"));
        SortOperation sortOperation = Aggregation.sort(sort);

        //组合条件
        Aggregation aggregation = Aggregation.newAggregation(ErrorInfo.class, matchOperation, groupOperation, sortOperation);

        // 执行操作
        AggregationResults<Map> aggregationResults = template.aggregate(aggregation, ErrorInfo.class, Map.class);

        return new BaseResponse(aggregationResults.getMappedResults());
    }

    @Override
    public BaseResponse getErrorLogList(Pageable pageable, Predicate predicate) throws BaseException {
        Page<ErrorInfo> page = errinfoDAO.findAll(predicate, pageable);
        List list = new ArrayList();
        for (ErrorInfo errorInfo : page.getContent()) {
            ResErrLog resLog = new ResErrLog();
            BeanUtils.copyProperties(errorInfo, resLog);
            //MongoDB主键为组合主键，此处需要进行处理
            resLog.setId(errorInfo.getId().toString());
            list.add(resLog);
        }
        return new BaseResponse(list);
    }

    @Override
    public BaseResponse getErrorLogCount(Predicate predicate) throws BaseException {
        long count = errinfoDAO.count(predicate);
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse getErrorLogItem(String id) throws BaseException {
        return new BaseResponse(errinfoDAO.findOne(id));
    }
}
