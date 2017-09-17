package org.superboot.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.superboot.base.BaseResponse;
import org.superboot.pub.controller.BaseWebController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 15:25
 * @Path org.superboot.controller.IndexController
 */
@Controller
public class IndexController extends BaseWebController {

    @Value("${jwt.header}")
    private String tokenHeader;


    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {


        BaseResponse BaseMessage = new BaseResponse();

        //post请求数据
        //getRestTemplate().postForObject(getServerUrl() + "/auth/get_token", user, BaseMessage.class, message);

        //post请求数据
        //getRestTemplate().postForObject(getServerUrl() + "/auth/get_token", user, BaseResponse.class, BaseMessage);


        //post请求数据
        //JSONObject json = getRestTemplate().postForObject(getServerUrl() + "/auth/get_token", user, JSONObject.class, BaseMessage);


        return "index";
    }

    @RequestMapping("/hello")
    public String hello(Model model) {
        Map user = Maps.newHashMap();
        user.put("id", "sd");
        user.put("name", "cc");
        user.put("description", "一代枭雄");
        model.addAttribute("user", user);
        return "hello";
    }
}
