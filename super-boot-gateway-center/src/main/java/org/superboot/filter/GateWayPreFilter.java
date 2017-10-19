package org.superboot.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.superboot.base.BaseConstants;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.pub.Pub_RedisUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

/**
 * <b> 消息求情前处理类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/26
 * @time 16:41
 * @Path org.superboot.filter.GateWayPreFilter
 */
public class GateWayPreFilter extends ZuulFilter {


    @Autowired
    private Pub_RedisUtils pubRedisUtils;

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        //拦截请求并设置信任ID信息
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            ctx.addZuulRequestHeader(BaseConstants.GLOBAL_KEY, pubRedisUtils.setGlobalID());
        } catch (ParseException e) {
            throw new SuperBootException(SuperBootCode.EXCEPTION);
        }

        return null;
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public static JSONObject getRequestJsonString(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if ("GET".equals(submitMehtod)) {
            return new JSONObject();
        } else {
            return JSON.parseObject(getRequestPostStr(request));
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }


}
