package org.superboot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.zip.CRC32;

/**
 * <b> 加密工具类 </b>
 * <p>
 * 功能描述:提供常用加密算法
 * </p>
 */
public class EncryptUtils {


    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHexString(final byte[] bs) {
        final int len;
        if (bs != null && (len = bs.length) != 0) {
            final char[] cs = new char[len << 1];
            final char[] myDigits = DIGITS;
            byte b;
            for (int i = 0, j = 0; i < len; i++) {
                cs[j++] = myDigits[((b = bs[i]) >>> 4) & 0xF];
                cs[j++] = myDigits[b & 0xF];
            }
            return String.valueOf(cs);
        }
        return null;
    }

    private static String digest(String name, String source)
            throws NoSuchAlgorithmException {
        if (source != null) {
            final MessageDigest md = MessageDigest.getInstance(name);
            md.update(source.getBytes());
            return toHexString(md.digest());
        }
        return null;
    }

    private static String digest(String name, byte[] bytes)
            throws NoSuchAlgorithmException {
        if (bytes != null) {
            final MessageDigest md = MessageDigest.getInstance(name);
            md.update(bytes);
            return toHexString(md.digest());
        }
        return null;
    }

    public static String SHA1_HEX(String source) {
        try {
            return digest("SHA-1", source);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    public static String SHA1_HEX(byte[] bytes) {
        try {
            return digest("SHA-1", bytes);
        } catch (NoSuchAlgorithmException bytesz) {
        }
        return null;
    }

    public static String MD5_HEX(String source) {
        try {
            return digest("MD5", source);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    public static String MD5_HEX(byte[] bytes) {
        try {
            return digest("MD5", bytes);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public static Long crc32(String name) {
        CRC32 crc32 = new CRC32();
        crc32.update(name.getBytes());
        return crc32.getValue();
    }

    public static void main(String[] args) {
        System.out.println(MD5_HEX("1001B1FB3E48588FCB35C3BA3ACA636858280CA175B9"));
        System.out.println(crc32("'3A1B31BD'"));
    }
}
