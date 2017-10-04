package org.superboot.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.superboot.utils.ReflectionUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <b> 项目共用方法类 </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/10
 * @time 15:48
 * @Path org.superboot.pub.utils.Pub_Utils
 */
@Component
public class Pub_Utils {

    @Autowired
    private Pub_Tools pubTools;
    /**
     * 获取主键列的字段名称
     *
     * @param o 实体对象
     * @return
     */
    public  String getIdField(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            //判断是否有ID的注解
            if (null != field.getAnnotation(Id.class)) {
                return field.getName();
            }
        }
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            //判断是否为ID的注解方法
            if (null != method.getAnnotation(Id.class)) {
                //获取字段的值
                String filedName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                return filedName;
            }
        }

        return null;
    }

    /**
     * 对实体的主键列设置值
     *
     * @param o 实体类
     * @return
     */
    public  Object setIdValue(Object o) {
        //获取主键列所在字段
        String field = getIdField(o);
        if (null != field) {
            //获取字段的值
            Object val = ReflectionUtils.getFieldValue(o, field);
            if (null == val) {
                //设置主键
                ReflectionUtils.setFieldValue(o, field, pubTools.genUUID());
            }
        }
        return o;
    }

}
