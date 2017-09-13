package org.superboot.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b> 类映射工具 </b>
 * <p>
 * 功能描述:提供基于类名、方法、字段的映射与解析
 * </p>
 *
 * @author jesion
 * @date 2017/9/5
 * @time 17:23
 * @Path org.superboot.utils.ReflectionUtils
 */
public class ReflectionUtils {

    /**
     * 实例化方法
     * @param object 类名
     * @param methodName 方法名称
     * @param parameterTypes 参数类型
     * @return
     */
    private static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 执行方法
     * @param object 类名
     * @param methodName 方法名称
     * @param parameterTypes 参数类型
     * @param parameters 参数值
     * @return
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            System.err.println(object.getClass() + "未获取到" + methodName + "方法!");
            return null;
        }

        method.setAccessible(true);
        try {
            if (method != null) {
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取常量字段
     * @param object 类名
     * @param fieldName 字段名称
     * @return
     */
    private static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class clazz = object.getClass();

        while (clazz != Object.class) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

    /**
     * 对类的常量设置值
     * @param object 类名
     * @param fieldName 字段名称
     * @param value 值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            System.err.println(object.getClass() + "未获取到" + fieldName + "属性!");
            return;
        }

        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类常量的值
     * @param object 类名
     * @param fieldName 字段名称
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            System.err.println(object.getClass() + "未获取到" + fieldName + "属性!");
            return null;
        }

        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
