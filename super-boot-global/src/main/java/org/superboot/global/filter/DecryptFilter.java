package org.superboot.global.filter;

import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.core.annotation.Order;
import org.superboot.base.BaseConstants;
import org.superboot.global.wrapper.RequestBodyDecryptWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <b> 解密过滤器 </b>
 * <p>
 * 功能描述:提供对上行信息的解密操作
 * </p>
 */
@Order(-1)
//自动注册Filter类
@WebFilter(filterName = "DecryptFilter", urlPatterns = "/*")
public class DecryptFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //判断是否进行了数据加密
        String requestType = req.getHeader(BaseConstants.SECRET_KEY);
        if (StrUtil.isNotBlank(requestType)) {
            //执行数据解密
            chain.doFilter(new RequestBodyDecryptWrapper(req), response);
        } else {
            chain.doFilter(request, response);
        }
    }


}
