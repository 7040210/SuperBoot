package org.superboot.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.superboot.base.BaseException;
import org.superboot.base.StatusCode;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.common.pub.Pub_WebUtils;

import java.io.IOException;

/**
 * <b> 网关出现异常的时候处理 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class GateWayErrorFilter extends SendErrorFilter {

    @Autowired
    private Pub_WebUtils webUtils;

    @Autowired
    private Pub_Tools pubTools;

    @Override
    public int filterOrder() {
        return 30;
    }

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //返回数据之前清理
        RequestContext ctx = RequestContext.getCurrentContext();

        Throwable throwable = ctx.getThrowable();

        //请求超过限流数
        if (HttpStatus.TOO_MANY_REQUESTS.value() == ctx.getResponseStatusCode()) {
            ctx.setResponseStatusCode(StatusCode.RATELIMIT_ERR.getCode());
            ctx.setResponseBody(JSON.toJSON(pubTools.genNoMsg(StatusCode.RATELIMIT_ERR)).toString());
        }
        ctx.setSendZuulResponse(false);

        //记录异常日志
        try {
            webUtils.saveErrLog(ctx.getRequest(), throwable);
        } catch (IOException e) {
            throw new BaseException(StatusCode.EXCEPTION);
        }

        return null;
    }
}
