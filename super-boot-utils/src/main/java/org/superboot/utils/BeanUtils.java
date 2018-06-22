package org.superboot.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

/**
 * <b> BeanUtils </b>
 * <p>
 * 功能描述:bean对象工具类，继承spring BeanUtils，重写copyProperties方法，处理源对象为空问题
 * </p>
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
    /**
     * 复制对象属性
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws BeansException {
        //源对象空处理
        if (null != source) {
            //扩展 支持复制Map类型的源对象数据
            if (source instanceof Map) {
                copyProperties((Map) source, target);
            } else if (target instanceof Map) {
                copyProperties(source, (Map) target);
            } else {
                org.springframework.beans.BeanUtils.copyProperties(source, target);
            }
        }
    }

    /**
     * 复制Map属性
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Map source, Object target) throws BeansException {
        Iterator<Map.Entry> s = source.entrySet().iterator();
        //获取目标对象所有属性
        PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor targetPd : targetPds) {
            //获取目标对象属性写方法
            Method writeMethod = targetPd.getWriteMethod();
            if (null != writeMethod) {
                try {
                    while (s.hasNext()) {
                        Map.Entry en = s.next();
                        if (en.getKey().equals(targetPd.getName())) {
                            //如果方法访问权限不足，设置方法允许访问权限
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, new Object[]{en.getValue()});
                        }
                    }
                } catch (Throwable var15) {
                    throw new FatalBeanException("Could not copy property \'" + targetPd.getName() + "\' from source to target", var15);
                }
            }
        }
    }

    /**
     * 复制实体属性到map
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Map target) throws BeansException {
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
        for (PropertyDescriptor sourcePd :
                sourcePds) {
            Method readMethod = sourcePd.getReadMethod();
            if (null != readMethod) {

                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    target.put(sourcePd.getName(), value);
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + sourcePd.getName() + "' from source to target", ex);
                }
            }
        }
    }
}
