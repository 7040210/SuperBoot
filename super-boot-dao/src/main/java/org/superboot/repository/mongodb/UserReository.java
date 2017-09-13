package org.superboot.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.superboot.entity.base.api.RegisterUser;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 8:42
 * @Path org.superboot.repository.mongodb.UserReository
 */
public interface  UserReository  extends MongoRepository<RegisterUser,Long>{
}
