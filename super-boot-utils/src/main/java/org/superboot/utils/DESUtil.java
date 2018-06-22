package org.superboot.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * <b> DES加密算法 </b>
 * <p>
 * 功能描述:JAVA版与C#通用的DES加密解密工具
 * </p>
 */
public class DESUtil {

    /**
     * 秘钥
     */
    private static final String PASSWORD_CRYPT_KEY = "70402100";

    /**
     * <b> 功能 : 加密方法 </b>
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     * @作者 张帅
     * @创建日期 2014-8-28
     */
    public static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    /**
     * <b> 功能 : 解密方法 </b>
     *
     * @param message
     * @return
     * @throws Exception
     * @作者 张帅
     * @创建日期 2014-8-28
     */
    public static String decrypt(String message) throws Exception {
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(PASSWORD_CRYPT_KEY.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    /**
     * <b> 功能 : 加密方法 </b>
     *
     * @param value
     * @return
     * @作者 张帅
     * @创建日期 2014-8-28
     */
    public static String encrypt(String value) {
        String result = "";
        try {
            value = java.net.URLEncoder.encode(value, "utf-8");
            result = toHexString(encrypt(value, PASSWORD_CRYPT_KEY))
                    .toUpperCase();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return result;
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString + "";
    }


    public static void main(String[] args) throws Exception {
        //待加密数据
        String value = "CU1408200003R1";
        //加密
        String a = encrypt(value);
        System.out.println("加密后的数据为:" + a);
        //解密
        System.out.println("解密后的数据为:" + decrypt(a));
    }

}
