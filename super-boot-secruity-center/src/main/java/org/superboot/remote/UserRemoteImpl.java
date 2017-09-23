package org.superboot.remote;

import org.springframework.stereotype.Component;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.entity.LoginUser;

/**
 * <b> 当远程接口不可用时通过此次实习降级容错处理 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/22
 * @time 21:58
 * @Path org.superboot.remote.UserRemote
 */
@Component
public class UserRemoteImpl implements UserRemote {

    @Override
    public BaseResponse createToken(LoginUser loginUser) throws SuperBootException {
        return  new BaseResponse(SuperBootCode.ACCOUNT_DISABLED);
    }
}