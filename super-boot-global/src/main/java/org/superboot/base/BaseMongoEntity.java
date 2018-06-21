package org.superboot.base;

import com.querydsl.core.annotations.QuerySupertype;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;


/**
 * <b> MongoDb实体基类 </b>
 * <p>
 * 功能描述:所有Mongo数据库操作实体继承此基类，提供基本的Bean功能及QueryDSL支持
 * </p>
 */
@QuerySupertype
public class BaseMongoEntity {

    @Id
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseMongoEntity other = (BaseMongoEntity) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }

        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
