package org.superboot.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <b> 脱敏工具类 </b>
 * <p>
 * 功能描述:提供对一些敏感信息的脱敏操作
 * </p>
 */
public class SensitiveInfoUtils {

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName 全名
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param familyName 姓
     * @param givenName  名
     * @return
     */
    public static String chineseName(String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param id 身份证号
     * @return
     */
    public static String idCardNum(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.leftPad(num, StringUtils.length(id), "*");
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num
     * @return
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138****1234>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        if (7 < num.length()) {
            return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
        } else {
            return StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*");
        }
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address       地址信息
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email 邮箱地址
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }

    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code
     * @return
     */
    public static String cnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }


    /**
     * [自定义脱敏规则] 定义自定义脱敏规则，指定前面位数与后面位数，其他均星号隐藏
     *
     * @param code        代脱敏字符
     * @param beginLength 前面位数
     * @param endLength   后面位数
     * @return
     */
    public static String other(String code, int beginLength, int endLength) {
        return StringUtils.left(code, beginLength).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(code, endLength), StringUtils.length(code), "*"), "***"));
    }

    /**
     * 获取类全部方法
     *
     * @param clazz
     * @return
     */
    private static Method[] findAllMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        return methods;
    }


    /**
     * 获取全部字段
     *
     * @param clazz
     * @return
     */
    private static Field[] findAllField(Class<?> clazz) {
        Field[] fileds = clazz.getDeclaredFields();
        while (null != clazz.getSuperclass() && !Object.class.equals(clazz.getSuperclass())) {
            fileds = (Field[]) ArrayUtils.addAll(fileds, clazz.getSuperclass().getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fileds;
    }

    /**
     * 执行对象脱敏操作
     *
     * @param javaBean JAVA对象
     * @throws IllegalAccessException
     */
    private static void replace(Object javaBean) throws IllegalAccessException {
        Field[] fields = findAllField(javaBean.getClass());
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                Sensitive annotation = field.getAnnotation(Sensitive.class);
                if (field.getType().equals(String.class) && null != annotation) {
                    String valueStr = (String) field.get(javaBean);
                    if (StringUtils.isNotBlank(valueStr)) {
                        switch (annotation.type()) {
                            case CHINESE_NAME: {
                                field.set(javaBean, SensitiveInfoUtils.chineseName(valueStr));
                                break;
                            }
                            case ID_CARD: {
                                field.set(javaBean, SensitiveInfoUtils.idCardNum(valueStr));
                                break;
                            }
                            case FIXED_PHONE: {
                                field.set(javaBean, SensitiveInfoUtils.fixedPhone(valueStr));
                                break;
                            }
                            case MOBILE_PHONE: {
                                field.set(javaBean, SensitiveInfoUtils.mobilePhone(valueStr));
                                break;
                            }
                            case ADDRESS: {
                                field.set(javaBean, SensitiveInfoUtils.address(valueStr, annotation.endLength()));
                                break;
                            }
                            case EMAIL: {
                                field.set(javaBean, SensitiveInfoUtils.email(valueStr));
                                break;
                            }
                            case BANK_CARD: {
                                field.set(javaBean, SensitiveInfoUtils.bankCard(valueStr));
                                break;
                            }
                            case CNAPS_CODE: {
                                field.set(javaBean, SensitiveInfoUtils.cnapsCode(valueStr));
                                break;
                            }
                            case OTHER: {
                                field.set(javaBean, SensitiveInfoUtils.other(valueStr, annotation.beginLength(), annotation.endLength()));
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }

        }
    }


    /**
     * 对象脱敏
     *
     * @param javaBean 需要脱敏的对象
     * @return
     */
    public static void sensitiveObject(Object javaBean) throws IllegalAccessException {
        if (null != javaBean) {
            Class<? extends Object> raw = javaBean.getClass();
            if (raw.isInterface()) {
                return;
            }
            //处理集合
            if (javaBean instanceof List) {
                List list = ((List) javaBean);
                for (int i = 0; i < list.size(); i++) {
                    replace(list.get(i));
                }
            } else {
                replace(javaBean);
            }
        }
    }
}
