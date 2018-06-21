package org.superboot.global.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.StatusCode;
import org.superboot.utils.AESUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.Set;

/**
 * <b> 重构上行请求信息，对上行内容进行解密操作 </b>
 * <p>
 * 功能描述:重构上行请求信息，主要是对已经加密后的Body内容进行统一解密操作
 * </p>
 */
public class RequestBodyDecryptWrapper extends HttpServletRequestWrapper {
    private Logger log = LoggerFactory.getLogger(getClass());


    private byte[] requestBody = null;

    public RequestBodyDecryptWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = new byte[0];
        }
        //变更请求内容
        setRequestBody();
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return super.getHeaderNames();
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return super.getHeaders(name);
    }

    /**
     * 将RequestBody参数转为字符串
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getRequestBody() throws UnsupportedEncodingException {
        return (new String(requestBody, "UTF-8"));
    }

    /**
     * 变更Json对象内容信息
     *
     * @throws UnsupportedEncodingException
     */
    public void setRequestBody() throws UnsupportedEncodingException {
        //读取Body内容后变更为对象
        String body = getRequestBody();
        if (StrUtil.isNotBlank(body)) {
            Object json = null;
            //扑捉转换异常调用JSON转换
            try {
                json = JSON.parse(body);
            } catch (Exception e) {
                json = JSON.toJSON(body);
            }
            String secretKey = getHeader(BaseConstants.SECRET_KEY);

            log.debug("--------------执行密钥解密------------------");
            //执行RSA解密
            try {
                secretKey = rsaDecrypt(secretKey);
            } catch (Exception e) {
                throw new BaseException(StatusCode.DECODE_FAIL);
            }
            log.debug("--------------执行数据解密------------------");
            if (StrUtil.isNotBlank(secretKey)) {
                //判断是否为数组
                if (json instanceof JSONArray) {
                    JSONArray jsonArray = JSON.parseArray(json.toString());
                    jsonArray = aesJsonArray(secretKey, jsonArray);
                    //返回变更后的对象信息
                    this.requestBody = jsonArray.toJSONString().getBytes("UTF-8");
                } else if (json instanceof JSONObject) {
                    JSONObject jsonObject = JSON.parseObject(json.toString());
                    jsonObject = aesJsonObject(secretKey, jsonObject);
                    //返回变更后的对象信息
                    this.requestBody = jsonObject.toJSONString().getBytes("UTF-8");
                } else {
                    //非JSON对象直接返回到后台业务内处理
                    this.requestBody = json.toString().getBytes("UTF-8");
                }

            }
        }

    }

    /**
     * 解密JSON数组对象
     *
     * @param secretKey 密钥
     * @param jsonArray JSON数组
     * @return
     */
    public JSONArray aesJsonArray(String secretKey, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            //判断是否为JSON对象
            if (jsonArray.get(i) instanceof JSONObject) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //变更JSON数组对象
                jsonArray.set(i, aesJsonObject(secretKey, jsonObject));
            } else {
                //非JSON对象直接执行解密
                jsonArray.set(i, aesDecrypt(secretKey, jsonArray.getString(i)));
            }

        }
        return jsonArray;
    }


    /**
     * 解密JSON对象
     *
     * @param secretKey  密钥
     * @param jsonObject JSON数组
     * @return
     */
    public JSONObject aesJsonObject(String secretKey, JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            //获取内部嵌套对象是否为数组，如果是数组则进行数组循环解密
            Object jsonItem = jsonObject.get(key);
            if (null != jsonItem) {
                if (jsonItem instanceof JSONArray) {
                    JSONArray jsonArray = JSON.parseArray(jsonItem.toString());
                    jsonArray = aesJsonArray(secretKey, jsonArray);
                    //便跟对象内容
                    jsonObject.put(key, jsonArray);
                } else {
                    //获取内容
                    String value = jsonItem.toString();
                    //执行AES解密
                    value = aesDecrypt(secretKey, value);
                    //便跟对象内容
                    jsonObject.put(key, value);
                }
            }

        }

        return jsonObject;
    }


    /**
     * 执行数据解密
     *
     * @param secretKey 密钥
     * @param value     加密后内容
     * @return
     */
    public String aesDecrypt(String secretKey, String value) {
        //解密密文信息
        try {
            String deValue = AESUtil.aesDecrypt(value, secretKey);
            log.debug("--------------执行字段解密------------------");
            return deValue;
        } catch (Exception e) {
            return value;
        }
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

}