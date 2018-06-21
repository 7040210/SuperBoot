package org.superboot.common.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.superboot.base.*;
import org.superboot.dao.jpa.AuthorizationDAO;
import org.superboot.entity.jpa.SuperbootAuthorization;
import org.superboot.utils.AESUtil;
import org.superboot.utils.DateUtils;
import org.superboot.utils.ReflectionUtils;
import org.superboot.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <b> 项目共用的方法 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Component
public class Pub_Tools {
    @Resource
    Pub_LocalTools local;
    @Autowired
    Pub_RedisUtils redisUtils;
    SnowflakeIdWorker idWorker0 = null;
    /**
     * 工作中心ID 0-31
     */
    @Value("${Snowflake.workerId}")
    private long workerId;
    /**
     * 数据中心ID 0-31
     */
    @Value("${Snowflake.datacenterId}")
    private long datacenterId;
    @Autowired
    private AuthorizationDAO authorizationDAO;

    /**
     * 特殊字符转义，实现对一些特殊字符的转义操作，防止处理的时候出错
     *
     * @param text
     * @return
     */
    public static String transfString(String text) {

        if (null != text) {
            text = text.replace(">", "&gt;");
            text = text.replace("<", "&lt;");
            text = text.replace(" ", "&nbsp;");
            text = text.replace("\"", "&quot;");
            text = text.replace("\'", "&#39;");
            text = text.replace("\\", "\\\\");
            text = text.replace("\n", "\\n");
            text = text.replace("\r", "\\r");
        }

        return text;
    }

    /**
     * 转义还原，在RSA解密的时候转义的字符需要还原，否则会解密出错
     *
     * @param text
     * @return
     */
    public static String transfBack(String text) {

        if (null != text) {
            text = text.replace("&gt;", ">");
            text = text.replace("&lt;", "<");
            text = text.replace("&nbsp;", " ");
            text = text.replace("&quot;", "\"");
            text = text.replace("&#39;", "\'");
            text = text.replace("\\\\", "\\");
            text = text.replace("\\n", "\n");
            text = text.replace("\\r", "\r");
        }

        return text;
    }

    /**
     * 生成全局唯一标识
     *
     * @return
     */
    public long genUUID() {
        if (null == idWorker0) {
            idWorker0 = new SnowflakeIdWorker(workerId, datacenterId);
        }
        return idWorker0.nextId();
    }

    /**
     * 获取主键列的字段名称
     *
     * @param o 实体对象
     * @return
     */
    public String getIdField(Object o) {
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
    public Object setIdValue(Object o) {
        //获取主键列所在字段
        String field = getIdField(o);
        if (null != field) {
            //设置主键
            ReflectionUtils.setFieldValue(o, field, genUUID());
        }
        return o;
    }

    /**
     * 获取主键列值
     *
     * @param o 实体类
     * @return
     */
    public Object getIdValue(Object o) {
        //获取主键列所在字段
        String field = getIdField(o);
        if (null != field) {
            //获取字段的值
            return ReflectionUtils.getFieldValue(o, field);
        }
        return null;
    }

    /**
     * 获取对象方法上的注解信息
     *
     * @param o               对象信息
     * @param annotationClass 注解类
     * @param methodName      方法名称
     * @return
     */
    public Annotation getMothodAnnotationByAnnotation(Object o, Class annotationClass, String methodName) {
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            //获取传入的方法
            if (method.getName().toLowerCase().equals(methodName.toLowerCase())) {

                //判断是否为ID的注解方法
                if (null != method.getAnnotation(annotationClass)) {
                    return method.getAnnotation(annotationClass);
                }
            }
        }
        return null;
    }

    /**
     * 获取字段上的注解信息
     *
     * @param o               对象信息
     * @param annotationClass 注解类
     * @param fieldName       字段名称
     * @return
     */
    public Annotation getFieldAnnotationByAnnotation(Object o, Class annotationClass, String fieldName) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取传入的字段
            if (field.getName().toLowerCase().equals(fieldName.toLowerCase())) {

                //判断是否为ID的注解方法
                if (null != field.getAnnotation(annotationClass)) {
                    return field.getAnnotation(annotationClass);
                }
            }
        }
        return null;
    }

    /**
     * 通过反射的方式设置字段的值
     *
     * @param fieldName 字段名称
     * @param value     字段值
     * @param o         实体对象
     * @return
     */
    public Object setFieldValue(String fieldName, Object value, Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().endsWith(fieldName)) {
                ReflectionUtils.setFieldValue(o, fieldName, value);
            }
        }
        return o;
    }

    /**
     * 通过反射的方式设置字段的值
     *
     * @param fieldName 字段名称
     * @param o         实体对象
     * @return
     */
    public Object getFieldValue(String fieldName, Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().endsWith(fieldName)) {
                return ReflectionUtils.getFieldValue(o, fieldName);
            }
        }
        return null;
    }

    /**
     * 通过注解识别需要处理的字段，然后进行加解密操作
     *
     * @param o         类对象
     * @param secretKey 密钥
     * @param aseType   0为解密 1为加密
     * @return
     */
    public Object aesAlgorithm(Object o, String secretKey, int aseType) throws BadPaddingException {
        //如果传入是数组则进行数据循环解密
        if (o instanceof List || o instanceof ArrayList) {
            List list = (List) o;
            for (int i = 0; i < list.size(); i++) {
                for (Class<?> superClass = list.get(i).getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
                    Field[] fields = superClass.getDeclaredFields();
                    for (Field field : fields) {
                        //如果是整数、小数等类型则不进行解密操作
                        if (long.class == field.getType()
                                || int.class == field.getType()
                                || double.class == field.getType()
                                || Long.class == field.getType()
                                || Integer.class == field.getType()
                                || Double.class == field.getType()) {
                            continue;
                        }
                        String val = "" + ReflectionUtils.getFieldValue(list.get(i), field.getName());
                        //执行解密操作
                        Object value = null;
                        try {
                            if (0 == aseType) {
                                value = AESUtil.aesDecrypt(val, secretKey);
                            } else {
                                value = AESUtil.aesEncrypt(val, secretKey);
                            }

                        } catch (Exception e) {
                            throw new BaseException(StatusCode.DECODE_FAIL);
                        }
                        if (null != value) {
                            ReflectionUtils.setFieldValue(list.get(i), field.getName(), value);
                        }
                    }
                }
            }
            o = list;
        } else if (o instanceof BaseRequest) {
            for (Class<?> superClass = o.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
                Field[] fields = superClass.getDeclaredFields();
                for (Field field : fields) {
                    //如果是整数、小数等类型则不进行解密操作
                    if (long.class == field.getType()
                            || int.class == field.getType()
                            || double.class == field.getType()
                            || Long.class == field.getType()
                            || Integer.class == field.getType()
                            || Double.class == field.getType()) {
                        continue;
                    }
                    String val = "" + ReflectionUtils.getFieldValue(o, field.getName());
                    //执行解密操作
                    Object value = null;
                    try {
                        if (0 == aseType) {
                            value = AESUtil.aesDecrypt(val, secretKey);
                        } else {
                            value = AESUtil.aesEncrypt(val, secretKey);
                        }

                    } catch (Exception e) {
                        throw new BaseException(StatusCode.DECODE_FAIL);
                    }
                    if (null != value) {
                        ReflectionUtils.setFieldValue(o, field.getName(), value);
                    }
                }
            }
        }

        return o;
    }

    /**
     * RSA解密
     *
     * @param text 密文
     * @return
     */
    public String rsaDecrypt(String text) throws Exception {

        //使用hutool进行RSA解密，这个的优势是免去使用第三方JAR包的依赖
        RSA rsa = new RSA(BaseConstants.DEFAULT_PKCS8_PRIVATE_KEY, BaseConstants.DEFAULT_PUBLIC_KEY);

        //URL转码
        return new String(rsa.decrypt(Base64.decode(text), KeyType.PrivateKey), CharsetUtil.UTF_8);
    }

    /**
     * RSA加密,加密后的内容会进行特殊字符的转码，解密的时候也需要将转义内容变成原文
     *
     * @param text 密文
     * @return
     */
    public String rsaEncrypt(String text) throws Exception {

        //使用hutool进行RSA解密，这个的优势是免去使用第三方JAR包的依赖
        RSA rsa = new RSA(BaseConstants.DEFAULT_PKCS8_PRIVATE_KEY, BaseConstants.DEFAULT_PUBLIC_KEY);
        //特殊字符转义
        return Base64.encode(rsa.encrypt(text, KeyType.PublicKey));
    }

    /**
     * 随机生成AES密钥
     *
     * @return
     */
    public String genAesKey() {
        return "key" + System.currentTimeMillis();
    }

    /**
     * 生成消息相应信息
     *
     * @param code    响应码
     * @param status  响应状态
     * @param message 消息内容 特殊处理的时候需要传默认使用code对应信息
     * @param data    消息体
     * @return
     */
    public BaseMessage genMsg(int code, int status, String message, Object data) {
        BaseMessage basemessage = new BaseMessage();
        basemessage.setCode(code);
        basemessage.setStatus(status);
        if (StringUtils.isBlank(message)) {
            basemessage.setMessage(local.getMessage(code));
        } else {
            basemessage.setMessage(message);
        }

        if (null != data) {
            //如果传入对象是个JSON 直接返回
            if (data instanceof JSONObject) {
                basemessage.setData(data);
            }
            //如果是JSON数组直接返回
            else if ((String.valueOf(data).trim().startsWith("[") && String.valueOf(data).trim().endsWith("]"))) {
                basemessage.setData(data);
            }
            //如果是JSON字符串也直接返回
            else if ((String.valueOf(data).trim().startsWith("{") && String.valueOf(data).trim().endsWith("}"))) {
                basemessage.setData(data);
            }
            //如果是个普通的字符串，则需要构建为JSON对象后返回
            else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("detail", data);
                basemessage.setData(jsonObject);
            }

        }

        return basemessage;
    }

    /**
     * 生成成功消息相应信息
     *
     * @param code    响应状态
     * @param message 响应结果
     * @param data    数据体
     * @return
     */
    public BaseMessage genOkMsg(int code, String message, Object data) {
        return genMsg(
                code,
                BaseStatus.OK.getCode(), message,
                data);
    }

    /**
     * 生成成功消息相应信息
     *
     * @param code 提示码
     * @param data 消息体
     * @return
     */
    public BaseMessage genOkMsg(int code, Object data) {
        return genOkMsg(
                code,
                null,
                data);
    }

    /**
     * 生成成功消息相应信息
     *
     * @param data 消息体
     * @return
     */
    public BaseMessage genOkMsg(Object data) {
        return genOkMsg(
                StatusCode.OK.getCode(),
                data);
    }

    /**
     * 生成成功消息相应信息
     *
     * @param sbc 状态
     * @return
     */
    public BaseMessage genOkMsg(StatusCode sbc) {
        return genOkMsg(
                sbc.getCode(),
                null);
    }

    /**
     * 生成失败消息相应信息
     *
     * @param code    响应码
     * @param message 消息提示
     * @param data    消息体
     * @return
     */
    public BaseMessage genNoMsg(int code, String message, Object data) {
        return genMsg(
                code,
                BaseStatus.NO.getCode(),
                message,
                data);
    }

    /**
     * 生成失败消息相应信息,使用国际化配置显示错误信息
     *
     * @param code 响应码
     * @param data 消息体
     * @return
     */
    public BaseMessage genNoMsg(int code, Object data) {
        return genNoMsg(
                code,
                null,
                data);
    }

    /**
     * 生成失败消息相应信息,使用国际化配置显示错误信息
     *
     * @param sc 异常信息
     * @return
     */
    public BaseMessage genNoMsg(StatusCode sc) {
        return genNoMsg(
                sc.getCode(), null);
    }

    /**
     * 生成失败消息相应信息,使用国际化配置显示错误信息
     *
     * @param code 异常编码
     * @return
     */
    public BaseMessage genNoMsg(int code) {
        return genNoMsg(
                code, null);
    }

    /**
     * 生成失败消息相应信息,使用国际化配置显示错误信息
     *
     * @param sc   异常信息
     * @param data 消息体
     * @return
     */
    public BaseMessage genNoMsg(StatusCode sc, Object data) {
        return genNoMsg(
                sc.getCode(),
                data);
    }

    /**
     * 执行第三方TOKEN信息校验
     *
     * @param token
     */
    public boolean checkOtherToken(String token) {

        String tokenStr = null;
        //解密Token信息
        try {
            tokenStr = rsaDecrypt(token);
            if (StrUtil.isNotBlank(tokenStr)) {
                if (40 != tokenStr.length()) {
                    throw new BaseException(StatusCode.TOKEN_INVALID);
                }
            }


        } catch (Exception e) {
            throw new BaseException(StatusCode.DECODE_FAIL);
        }

        if (StrUtil.isNotBlank(tokenStr)) {
            //安装长度拆分TOKEN信息
            String accessKey = tokenStr.substring(0, 20);

            String secretKey = tokenStr.substring(20);

            //根据条件从数据库查询数据
            SuperbootAuthorization authorization = authorizationDAO.findByAccessKeyAndSecretKey(accessKey, secretKey);
            if (null == authorization) {
                throw new BaseException(StatusCode.TOKEN_INVALID);
            }

            if (BaseConstants.DATA_STATUS_OK != authorization.getDr()) {
                throw new BaseException(StatusCode.TOKEN_INVALID);
            }

            //授权到期时间不为空则判断到期时间
            if (StrUtil.isNotBlank(authorization.getAuthEndDate())) {
                try {
                    if (0 > DateUtils.getDifferDays(authorization.getAuthEndDate(), DateUtils.getCurrentDate())) {
                        throw new BaseException(StatusCode.TOKEN_LOCKED);
                    }
                } catch (ParseException e) {
                    throw new BaseException(StatusCode.EXCEPTION);
                }
            }

            //设置TOKEN与公司对照关系
            redisUtils.setOtherToken(token, authorization.getPkGroup());
            return true;
        }

        return false;

    }


    /**
     * 将一个对象转换成JSON字符
     *
     * @param obj
     * @return
     */
    public String objectToJson(Object obj) {
        if (null == obj) {
            return "";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 将一个JSON对象转换成JAVA对象
     *
     * @param json JSON字符
     * @param c    返回实体对象
     * @return
     */
    public <T> T jsonToObject(String json, Class<T> c) {

        if (StrUtil.isBlank(json)) {
            return null;
        }
        return (T) JSON.toJavaObject((JSON) JSON.parse(json), c);
    }
}
