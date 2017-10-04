package org.superboot.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;

/**
 * <b> 通过统一网关进行服务调用 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/22
 * @time 21:58
 * @Path org.superboot.remote.UserRemote
 */
@FeignClient(name= "SUPER-BOOT-GATEWAY-CENTER")
public interface UserRemote {

    @RequestMapping(value = "/UserApi/users/login", method = RequestMethod.POST)
    BaseResponse createToken(@RequestBody @Validated LoginUser loginUser) throws SuperBootException;


}