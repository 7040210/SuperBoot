package org.superboot.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b> 校验工具类 </b>
 * <p>
 * 功能描述:提供一些简单的校验方法
 * </p>
 */
public class ValidUtils {


    private static final char[] enChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] numChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static {
        Arrays.sort(enChar);
        Arrays.sort(numChar);
    }

    /**
     * 是否为字母或数字
     *
     * @param test
     * @return
     */
    public static boolean isEnglishOrNumber(String test) {
        for (char ch : test.toCharArray()) {
            if ((Arrays.binarySearch(enChar, ch) < 0) && (Arrays.binarySearch(numChar, ch) < 0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为字母
     *
     * @param test
     * @return
     */
    public static boolean isEnglish(String test) {
        for (char ch : test.toCharArray()) {
            if (Arrays.binarySearch(enChar, ch) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为数字
     *
     * @param test
     * @return
     */
    public static boolean isNumber(String test) {
        for (char ch : test.toCharArray()) {
            if (Arrays.binarySearch(numChar, ch) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,5,8,7][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 是否为电话号码
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null;
        Pattern p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
}
