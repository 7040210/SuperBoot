package org.superboot.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.BaseToken;
import org.superboot.base.StatusCode;
import org.superboot.common.pub.Pub_RedisUtils;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.common.pub.Pub_WebUtils;
import org.superboot.common.utils.JWT_Utils;

import java.io.IOException;

/**
 * <b> 消息求情前处理类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class GateWayPreFilter extends ZuulFilter {

    Logger log = LoggerFactory.getLogger(getClass());


    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private JWT_Utils jwtUtils;

    @Autowired
    private Pub_Tools pubTools;

    @Autowired
    private Pub_WebUtils webUtils;

    @Autowired
    private Pub_RedisUtils redisUtils;


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
        startTime.set(System.currentTimeMillis());

        //生成消息ID用于调用链统计使用
        String messageId = null;

        //拦截请求并设置信任ID信息
        RequestContext ctx = RequestContext.getCurrentContext();


        //如果存在应用内部互相调用且通过网关的情况则消息ID需要传递下去
        if (ctx.getZuulRequestHeaders().containsKey(BaseConstants.GLOBAL_KEY.toLowerCase())) {
            messageId = ctx.getZuulRequestHeaders().get(BaseConstants.GLOBAL_KEY.toLowerCase());
        } else {
            messageId = String.valueOf(pubTools.genUUID());
        }

        ctx.getResponse().setCharacterEncoding("UTF-8");
        //设置返回类型
        ctx.getResponse().setContentType("application/json");

        String retMeg = "";

        log.debug("--------------执行Token验证------------------");
        //判断来源是否为第三方
        String oToken = ctx.getRequest().getHeader(BaseConstants.OTHER_TOKEN_KEY);
        if (StrUtil.isNotBlank(oToken)) {
            if (pubTools.checkOtherToken(oToken)) {
                log.debug("--------------Token验证通过------------------");
                redisUtils.setOtherMessId(messageId, oToken);
                //将消息ID向后传递
                ctx.addZuulRequestHeader(BaseConstants.OTHER_MESSAGE_ID.toLowerCase(), messageId);
                retMeg = JSON.toJSONString(pubTools.genOkMsg(StatusCode.TOKEN_SUCCESS));
            } else {
                JSONObject errMsg = (JSONObject) JSON.toJSON(pubTools.genNoMsg(StatusCode.TOKEN_INVALID));
                ctx.setResponseBody(errMsg.toJSONString());
                //设置消息不继续传递
                ctx.setSendZuulResponse(false);
                retMeg = errMsg.toJSONString();
            }
        } else {
            //检查是否调用的获取微服务的接口
            if (!ctx.getRequest().getRequestURI().endsWith("/v2/api-docs")) {
                BaseToken tokenInfo = jwtUtils.getTokenInfo(ctx.getRequest());
                //如果解析TOKEN出现异常则提示异常
                if (null != tokenInfo.getErrCode()) {
                    JSONObject errMsg = (JSONObject) JSON.toJSON(pubTools.genNoMsg(tokenInfo.getErrCode()));
                    ctx.setResponseBody(errMsg.toJSONString());
                    //设置消息不继续传递
                    ctx.setSendZuulResponse(false);
                    retMeg = errMsg.toJSONString();

                } else {
                    log.debug("--------------Token验证通过------------------");
                    redisUtils.setSessionInfo(messageId, tokenInfo);
                    //将消息ID向后传递
                    ctx.addZuulRequestHeader(BaseConstants.GLOBAL_KEY.toLowerCase(), messageId);
                    retMeg = JSON.toJSONString(pubTools.genOkMsg(StatusCode.TOKEN_SUCCESS));

                }
            }


        }


        //记录日志
        try {

            webUtils.saveLog(ctx.getRequest(), retMeg, messageId, null, (System.currentTimeMillis() - startTime.get()));
        } catch (IOException e) {
            //存储日志
            try {
                webUtils.saveErrLog(ctx.getRequest(), e);
            } catch (IOException e1) {
                throw new BaseException(StatusCode.EXCEPTION);
            }
        }


        return null;
    }


}
