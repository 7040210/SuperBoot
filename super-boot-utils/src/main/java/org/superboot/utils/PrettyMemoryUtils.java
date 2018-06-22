package org.superboot.utils;

/**
 * <b> 数据转换格式工具 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class PrettyMemoryUtils {

    private static final int UNIT = 1024;

    /**
     * @param byteSize 字节
     * @return
     */
    public static String prettyByteSize(long byteSize) {

        double size = 1.0 * byteSize;

        String type = "B";
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1KB
            type = "B";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1MB
            type = "KB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1GB
            type = "MB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1TB
            type = "GB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) { //不足1PB
            type = "TB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "PB";
            return format(size, type);
        }
        return ">PB";
    }

    private static String format(double size, String type) {
        int precision = 0;

        if (size * 1000 % 10 > 0) {
            precision = 3;
        } else if (size * 100 % 10 > 0) {
            precision = 2;
        } else if (size * 10 % 10 > 0) {
            precision = 1;
        } else {
            precision = 0;
        }

        String formatStr = "%." + precision + "f";

        if ("KB".equals(type)) {
            return String.format(formatStr, (size)) + "KB";
        } else if ("MB".equals(type)) {
            return String.format(formatStr, (size)) + "MB";
        } else if ("GB".equals(type)) {
            return String.format(formatStr, (size)) + "GB";
        } else if ("TB".equals(type)) {
            return String.format(formatStr, (size)) + "TB";
        } else if ("PB".equals(type)) {
            return String.format(formatStr, (size)) + "PB";
        }
        return String.format(formatStr, (size)) + "B";
    }

    public static void main(String[] args) {
        System.out.println(PrettyMemoryUtils.prettyByteSize(1023));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * UNIT));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * 1023));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * 1023 * 1023 * 1023));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT));
        System.out.println(PrettyMemoryUtils.prettyByteSize(1L * UNIT * UNIT * UNIT * UNIT * UNIT * UNIT));
    }

}
