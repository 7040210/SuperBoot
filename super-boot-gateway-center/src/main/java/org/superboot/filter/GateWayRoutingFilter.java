package org.superboot.filter;

import com.netflix.zuul.ZuulFilter;

/**
 * <b> 处理请求，进行路由 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/26
 * @time 16:42
 * @Path org.superboot.filter.GateWayRoutingFilter
 */
public class GateWayRoutingFilter extends ZuulFilter {

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
