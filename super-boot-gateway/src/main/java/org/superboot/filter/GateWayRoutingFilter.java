package org.superboot.filter;

import com.netflix.zuul.ZuulFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b> 处理请求，进行路由 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
public class GateWayRoutingFilter extends ZuulFilter {
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        return null;
    }
}
