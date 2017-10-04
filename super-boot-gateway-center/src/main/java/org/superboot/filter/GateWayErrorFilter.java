package org.superboot.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.superboot.base.BaseConstants;

/**
 * <b> 网关出现异常的时候处理 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/26
 * @time 16:43
 * @Path org.superboot.filter.GateWayPostFilter
 */
public class GateWayErrorFilter extends ZuulFilter {

    @Override
    public int filterOrder() {
        return 10;
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
        if(ctx.getZuulRequestHeaders().containsKey(BaseConstants.GLOBAL_KEY)){
            ctx.getZuulRequestHeaders().remove(BaseConstants.GLOBAL_KEY);
        }
        return null;
    }
}
