package org.superboot.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <b> RSA加密算法 </b>
 * <p>
 * 功能描述:
 * * LINUX生成秘钥方法
 * <p>
 * <p>
 * #生成1024字节秘钥
 * openssl genrsa -out rsa_private_key.pem 1024
 * #生成公钥
 * openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout
 * #如果要使用PKCS8编码执行下面语句
 * openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem -nocrypt
 * <p>
 * #查看秘钥
 * <p>
 * cat rsa_private_key.pem
 * cat rsa_public_key.pem
 * cat pkcs8_rsa_private_key.pem
 * </p>
 *
 * @author jesion
 * @date 2017/9/5
 * @time 17:23
 * @Path org.superboot.utils.RSAUtils
 */
public class RSAUtils {


    /**
     * 加密方式
     */
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";


    /**
     * String to hold name of the security provider.
     */
    private static final String PROVIDER = "BC";


    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 默认公钥
     */
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjxeQ8TmnvvIRreoJfTAdEdaD8" +
            "Vj/n8OQuAxqD6kbrYPculdAfPNLM5B5Y289oID74Ze8CTcy5vfQK1f5kgzKMr/Ey" +
            "wV3MMDVVjS05Z8/eQaU9xMiKeIqUkubAiL2oE/hNfBN/w/NTTGMpJ63x/yMdi6Uo" +
            "0FSFNm/6JmBeTflVJQIDAQAB";

    /**
     * 默认私钥
     */
    public static final String DEFAULT_PRIVATE_KEY = "MIICXAIBAAKBgQDjxeQ8TmnvvIRreoJfTAdEdaD8Vj/n8OQuAxqD6kbrYPculdAf\n" +
            "PNLM5B5Y289oID74Ze8CTcy5vfQK1f5kgzKMr/EywV3MMDVVjS05Z8/eQaU9xMiK\n" +
            "eIqUkubAiL2oE/hNfBN/w/NTTGMpJ63x/yMdi6Uo0FSFNm/6JmBeTflVJQIDAQAB\n" +
            "AoGAQMlb9u9S4Jbdj3ikNpj4hDd1F/Escjupm5DUbOspSvAkvsJsFUaGqzLDkwDK\n" +
            "yNJLNvo/dodMkRR/oslld499w88KFjG6kpf1ozfOvQvRs8J0sAJ/OfEN2jfluB+i\n" +
            "EkXwFoWTmztqi1KMCXljzrTYCX3YRQ/OtF8wcK3zWZWpMM0CQQD9tmBXAoJaxExK\n" +
            "4x4HEIrlcR276ywE8Gb6cmLWbm5sP43s3UiI43//imeAUu4DhFzbL9W2/58gxpJb\n" +
            "2NJ0cgOrAkEA5dOkNLRNaSqDj3hx1ZQyDhjRNCX/ZoRy0GsG8/cPPzwZh68qmIvs\n" +
            "AUEg1RxO/40kZMq0etOcvLpzObn+nh86bwJBAPIMWIUqQ6rMs+yOGUusIJcc/70U\n" +
            "c/6SS+yLzHe/C2ICkCh2RIk0Yh0tsejfNWvtj5kdXcskvG/Mgj1V8hJd49kCQBD0\n" +
            "m0G0J53hoBEenq9WHpdBp3WEdCI5FUthCgNHr91Hbs6+0pKsh/3TsztlukJKRGwX\n" +
            "NFd3czHNQ6D3otxBi4sCQBDXPGJWKeCWf/Bf1WGOypcxat+wFeloBHH5ix0fq2b5\n" +
            "IEHK2yWwMs6sfv8HXV2RLbDnW2FtcgDVTFmP4ZmbfCg=";

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;


    /**
     * 获取私钥
     *
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb + "");
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            Base64 base64Decoder = new Base64();
            byte[] buffer = base64Decoder.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb + "");
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 加载私钥 此处的私钥为 PKCS#8的私钥
     *
     * @param privateKeyStr
     * @throws Exception
     */
    public void loadPrivateKeyByPkcs8(String privateKeyStr) throws Exception {
        try {
            Base64 base64Decoder = new Base64();
            byte[] buffer = base64Decoder.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加载私钥 此处的私钥为 PKCS#1的私钥
     *
     * @param privateKeyStr
     * @throws Exception
     */
    public void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            Base64 base64Decoder = new Base64();
            byte[] buffer = base64Decoder.decode(privateKeyStr);
            RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
            RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, String plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData.getBytes());
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }


    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder + "";
    }


    /**
     * BASE64解密
     *
     * @param data 带解密数据
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data) throws Exception {
        Base64 base64Decoder = new Base64();
        return (new Base64()).decode(data);
    }


    /**
     * BASE64解密
     *
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] data) throws Exception {
        return (new Base64()).encode(data).toString();
    }

    public static void main(String[] args) {
        RSAUtils rsaEncrypt = new RSAUtils();
        // rsaEncrypt.genKeyPair();

        //加载公钥
        try {
            rsaEncrypt.loadPublicKey(RSAUtils.DEFAULT_PUBLIC_KEY);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }

        //加载私钥
        try {
            rsaEncrypt.loadPrivateKey(RSAUtils.DEFAULT_PRIVATE_KEY);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }

        //测试字符串
        String encryptStr = "中午吃的是什么啊";

        try {


            /*********************JAVA加密解密*****BEGIN*********************/
            //加密
            byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr);
            //解密
            byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);
            System.out.println("密文长度:" + cipher.length);
            System.out.println(RSAUtils.byteArrayToString(cipher));
            System.out.println("明文长度:" + plainText.length);
            System.out.println(RSAUtils.byteArrayToString(plainText));
            System.out.println(new String(plainText));

            /*********************JAVA加密解密****END**********************/


            /*********************JS解密*****BEGIN*********************/
            //JS加密后内容
            String msg = "ESj5aH21/C425rMI+qeQ1tEVfviKIicz+8MJugYrE73OeUSTkB4N8vLUlKTLWAxDskP+JFLj47Yzsah+wUL6QsHTgLCc/lYxrRxOiNS5Pnct+Vn1fx0fA72Z3WLck9XWQHuX0VkLE19XSJ51gjNrTDP5FJ8JzUEZjdbnVN4sDJY=";
            //解密JS内容
            plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), decryptBASE64(msg));
            //如果前端进行URL中文编码,此处进行URL解码
            System.out.println(URLDecoder.decode(new String(plainText), "UTF-8"));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
