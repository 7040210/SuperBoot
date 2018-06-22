package org.superboot.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <b> 随机数工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class RandomStrUtils {
    private static final String POSSIBLE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    private static final Random RANDOM = new Random();

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return "";
        }

        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " +
                    count + " is less than 0.");

        }
        if ((chars != null) && (chars.length == 0)) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if ((start == 0) && (end == 0)) {
            if (chars != null) {
                end = chars.length;
            } else if ((!(letters)) && (!(numbers))) {
                end = 2147483647;
            } else {
                end = 123;
                start = 32;
            }
        } else if (end <= start) {
            throw new IllegalArgumentException("Parameter end (" + end +
                    ") must be greater than start (" + start + ")");
        }
        char[] buffer = new char[count];
        int gap = end - start;
        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[(random.nextInt(gap) + start)];
            }
            if (((letters) && (Character.isLetter(ch))) || ((numbers) && (Character.isDigit(ch))) || ((!(letters)) && (!(numbers)))) {
                if ((ch >= 56320) && (ch <= 57343)) {
                    if (count == 0) {
                        ++count;
                    } else {
                        buffer[count] = ch;
                        --count;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }

                } else if ((ch >= 55296) && (ch <= 56191)) {
                    if (count == 0) {
                        ++count;
                    } else {
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        --count;
                        buffer[count] = ch;
                    }
                } else if ((ch >= 56192) && (ch <= 56319)) {
                    ++count;
                } else {
                    buffer[count] = ch;
                }
            } else {
                ++count;
            }
        }
        return new String(buffer);
    }

    public static String random(int count, String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, chars.toCharArray());
    }

    public static String random(int count, char[] chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }

    /**
     * 获取6位随机数字
     *
     * @return
     */
    public static String genRandom() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }


    /**
     * 生产一个指定长度的随机字符串
     *
     * @param length 字符串长度
     * @return
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
        }
        return sb.toString();
    }

}
