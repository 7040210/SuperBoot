package org.superboot.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b> 消息返回后处理类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
public class GateWayPostFilter extends ZuulFilter {
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //返回数据之前清理
        RequestContext ctx = RequestContext.getCurrentContext();

        return null;
    }
}
