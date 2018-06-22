package org.superboot.utils;

import com.xiaoleilu.hutool.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <b> 函数计算工具 </b>
 * <p>
 * 功能描述: 提供基础的函数计算，包含获取最大、最小、求和、平均
 * </p>
 */
public class FunctionUtils {
    /**
     * 计算一组数字的总和
     *
     * @param items 计算数组
     * @return
     */
    public static Object sum(List items) {
        if (0 == items.size()) {
            return null;
        }

        Object result = 0;

        for (Object item : items) {
            if (item instanceof Integer) {
                result = Integer.valueOf(result + "") + Integer.valueOf(item + "");
            }

            if (item instanceof Double) {
                result = Double.valueOf(result + "") + Double.valueOf(item + "");
            }

            if (item instanceof Long) {
                result = Long.valueOf(result + "") + Long.valueOf(item + "");
            }

        }
        return result;
    }


    /**
     * 计算一组数字的平均值,保留4位小数
     *
     * @param items 计算数组
     * @param scale 保留小数位数
     * @return
     */
    public static Object avg(List items, int scale) {
        if (0 == items.size()) {
            return null;
        }
        //计算求和
        Object result = sum(items);
        //如果是整形
        if (items.get(0) instanceof Integer) {
            return Integer.valueOf(result + "") / items.size();
        } else {
            return NumberUtil.round(Double.valueOf(result + "") / items.size(), scale);
        }

    }

    /**
     * 获取一组数字的最大值
     *
     * @param items
     * @return
     */
    public static Object max(List items) {
        if (0 == items.size()) {
            return null;
        }

        Object result = items.get(0);
        for (Object item : items) {
            if (item instanceof Integer) {
                if (Integer.valueOf(result + "") < Integer.valueOf(item + "")) {
                    result = item;
                }
            }

            if (item instanceof Double) {
                if (Double.valueOf(result + "") < Double.valueOf(item + "")) {
                    result = item;
                }
            }
            if (item instanceof Long) {
                if (Long.valueOf(result + "") < Long.valueOf(item + "")) {
                    result = item;
                }
            }

        }
        return result;
    }

    /**
     * 获取一组数字的最小值
     *
     * @param items
     * @return
     */
    public static Object min(List items) {
        if (0 == items.size()) {
            return null;
        }

        Object result = items.get(0);
        for (Object item : items) {
            if (item instanceof Integer) {
                if (Integer.valueOf(result + "") > Integer.valueOf(item + "")) {
                    result = item;
                }
            }

            if (item instanceof Double) {
                if (Double.valueOf(result + "") > Double.valueOf(item + "")) {
                    result = item;
                }
            }
            if (item instanceof Long) {
                if (Long.valueOf(result + "") > Long.valueOf(item + "")) {
                    result = item;
                }
            }

        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println(FunctionUtils.sum(list));

        System.out.println(FunctionUtils.avg(list, 2));

        System.out.println(FunctionUtils.max(list));

        System.out.println(FunctionUtils.min(list));


        List<Double> list1 = new ArrayList<>();
        list1.add(1.23456);
        list1.add(2.23456);
        list1.add(3.23456);


        System.out.println(FunctionUtils.sum(list1));

        System.out.println(FunctionUtils.avg(list1, 2));

        System.out.println(FunctionUtils.max(list1));

        System.out.println(FunctionUtils.min(list1));
    }
}
