package org.superboot.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b> 字符串工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class StringUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");


    /**
     * 生成唯一的key用于表主键
     *
     * @return
     */
    public static synchronized String genUUID() {
        //获取UUID
        String uuid = UUID.randomUUID() + "";
        //进行MD5加密
        return MD5Util.MD5(uuid).toUpperCase();
    }


    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        str = sb.toString();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        return str;
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 驼峰转下划线,效率比上面高
     *
     * @param str
     * @return
     */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuffer()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * object转String
     *
     * @param object
     * @return
     */
    public static String getString(Object object) {
        return getString(object, "");
    }

    public static String getString(Object object, String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return object.toString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转Integer
     *
     * @param object
     * @return
     */
    public static int getInt(Object object) {
        return getInt(object, -1);
    }

    public static int getInt(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * object转Boolean
     *
     * @param object
     * @return
     */
    public static boolean getBoolean(Object object) {
        return getBoolean(object, false);
    }

    public static boolean getBoolean(Object object, Boolean defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(object.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * <b> 功能 : 从一组值中得到不重复的值数组，一般用于如对于ID数组的过滤，滤掉重复值 </b>
     *
     * @param saOrgValue
     * @return
     * @作者 张帅
     * @创建日期 2011-9-8
     */

    public static String[] getDistinctArray(String[] saOrgValue) {
        if (saOrgValue == null) {
            return null;
        }

        HashMap mapRet = new HashMap();
        int iLen = saOrgValue.length;
        for (int i = 0; i < iLen; i++) {
            if (saOrgValue[i] != null) {
                mapRet.put(saOrgValue[i], "");
            }
        }
        if (mapRet.size() == 0) {
            return null;
        }
        return (String[]) mapRet.keySet().toArray(new String[mapRet.size()]);
    }

    /**
     * <b> 功能 : 根据字段长度计算下一个号码 </b>
     *
     * @param code   当前号码
     * @param length 字段长度
     * @return
     * @作者 张帅
     * @创建日期 2013-5-3
     */
    public static String getNextCode(String code, int length) {
        if (null != code && 0 < length) {
            String ncode = getString_TrimZeroLenAsNull(Integer.valueOf(code) + 1);
            int num = length - ncode.length();
            String zero = "";
            for (int i = 0; i < num; i++) {
                zero = zero + "0";
            }
            return zero + ncode;
        }
        return null;
    }

    /**
     * <b> 功能 : 根据一个对象的值得到String的值，如果为空串，返回空 </b>
     *
     * @param value 对象值
     * @return String的值
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getString_TrimZeroLenAsNull(Object value) {
        if (value == null || value.toString().trim().length() == 0 || "null".equals(value)) {
            return null;
        }
        return value.toString().trim();
    }

    /**
     * 小数四舍五入方法
     *
     * @param d 小数
     * @return
     */
    public static double doubleFormat(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(d));
    }

    /**
     * <b> 功能 : 判断两个字符串是否相等，均为NULL　或　TRIM＝NULL　认为相等 </b>
     *
     * @param sValue1 串1
     * @param sValue2 串2
     * @return 是否相等
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static boolean isEqual(String sValue1, String sValue2) {

        String sTemp1 = getString_TrimZeroLenAsNull(sValue1) == null ? ""
                : sValue1;
        String sTemp2 = getString_TrimZeroLenAsNull(sValue2) == null ? ""
                : sValue2;

        return sTemp1.equals(sTemp2);
    }

    /**
     * <b> 功能 : 得到一个哈希MAP中不存在的KEY </b>
     *
     * @param mapCheck 用于检查的HashMap
     * @param saKey    用于检查的KEY数组
     * @return
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String[] getNotExistedKeys(HashMap mapCheck, String[] saKey) {

        // 参数正确性检查
        if (mapCheck == null || saKey == null) {
            return null;
        }

        int iLen = saKey.length;
        HashMap mapQueryId = new HashMap();
        for (int i = 0; i < iLen; i++) {
            if (getString_TrimZeroLenAsNull(saKey[i]) != null
                    && !mapCheck.containsKey(saKey[i])) {
                mapQueryId.put(saKey[i], "");
            }
        }

        int iQueryLen = mapQueryId.size();
        if (iQueryLen == 0) {
            return null;
        }
        return (String[]) mapQueryId.keySet().toArray(new String[iQueryLen]);
    }


    /**
     * <b> 功能 : 给定一个域及其值数组，返回一个WHERE串，如给定“po_order.corderid”{"1","2","3"} 则返回
     * (po_order.corderid='1' OR po_order.corderid='2' OR po_order.corderid='3')
     * 主要用于查询中拼SQL条件 </b>
     *
     * @param sFieldName 域名称
     * @param oaValue    域的值数组
     * @return 拼好的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getORWhereByValues(String sFieldName, Object[] oaValue) {
        // 参数正确性检查
        if (sFieldName == null || oaValue == null) {
            return null;
        }
        // 支持权限查询
        if (oaValue.length == 1
                && oaValue[0].toString().toLowerCase().indexOf("select") >= 0) {
            return sFieldName + " in " + oaValue[0] + " ";
        }
        StringBuffer sbufOR = new StringBuffer();
        boolean bString = (oaValue[0] instanceof String) ? true : false;
        String sOR = " OR ";
        sbufOR.append("(");

        int iLen = oaValue.length;
        for (int i = 0; i < iLen; i++) {
            sbufOR.append(sFieldName);
            sbufOR.append("=");
            if (bString) {
                sbufOR.append("'");
            }
            sbufOR.append(oaValue[i]);
            if (bString) {
                sbufOR.append("'");
            }
            sbufOR.append(sOR);
        }
        sbufOR.delete(sbufOR.length() - sOR.length(), sbufOR.length() - 1);
        sbufOR.append(")");

        return sbufOR.toString();
    }

    /**
     * <b> 功能 : 给定一个域及其值数组，返回一个LIKE WHERE串，如给定“po_order.corderid”{"1","2","3"}
     * 则返回 (po_order.corderid like '1' OR po_order.corderid like '2' OR
     * po_order.corderid like '3') 主要用于查询中拼SQL条件 </b>
     *
     * @param sFieldName 域名称
     * @param oaValue    域的值数组
     * @return 拼好的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getORWhereByLikeValues(String sFieldName,
                                                Object[] oaValue) {
        // 参数正确性检查
        if (sFieldName == null || oaValue == null) {
            return null;
        }
        // 支持权限查询
        if (oaValue.length == 1
                && oaValue[0].toString().toLowerCase().indexOf("select") >= 0) {
            return sFieldName + " in " + oaValue[0] + " ";
        }
        StringBuffer sbufOR = new StringBuffer();
        boolean bString = (oaValue[0] instanceof String) ? true : false;
        String sOR = " OR ";
        sbufOR.append("(");

        int iLen = oaValue.length;
        for (int i = 0; i < iLen; i++) {
            sbufOR.append(sFieldName);
            sbufOR.append(" like");
            if (bString) {
                sbufOR.append("'");
            }
            sbufOR.append(oaValue[i]);
            if (bString) {
                sbufOR.append("'");
            }
            sbufOR.append(sOR);
        }
        sbufOR.delete(sbufOR.length() - sOR.length(), sbufOR.length() - 1);
        sbufOR.append(")");

        return sbufOR.toString();
    }

    /**
     * <b> 功能 : 给定一个字段数组，返回一个相应的字符串，如给定{"1","2","3"} 则返回 "1,2,3"
     * 主要用于查询中拼SELECT条件 </b>
     *
     * @param saField 域数组
     * @return 拼好的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getSelectStringByFields(String[] saField) {

        return getSelectStringByFields(null, saField);
    }

    /**
     * <b> 功能 : 给定一个int数组，返回一个相应的字符串，如给定{1,2,3} 则返回 "1,2,3" </b>
     *
     * @param iaField 域数组
     * @return 拼好的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getSelectStringByFields(int[] iaField) {
        String strRet = "";
        if (iaField == null || iaField.length == 0) {
            return strRet;
        }
        int iLen = iaField.length;
        strRet += iaField[0];
        for (int i = 1; i < iLen; i++) {
            strRet += ",";
            strRet += iaField[i];
        }
        return strRet;
    }

    /**
     * <b> 功能 : 给定一个字段数组，返回一个相应的字符串，如给定{"a"},{"1","2","3"} 则返回 "a.1,a.2,a.3"
     * 主要用于查询中拼SELECT条件 </b>
     *
     * @param sPrefix 前缀，应为表名
     * @param saField 域数组
     * @return 拼好的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getSelectStringByFields(String sPrefix,
                                                 String[] saField) {
        // 参数正确性检查
        if (saField == null) {
            return null;
        }

        StringBuffer sbufOR = new StringBuffer();

        int iLen = saField.length;
        for (int i = 0; i < iLen; i++) {
            if (sPrefix != null) {
                sbufOR.append(sPrefix);
                sbufOR.append(".");
            }
            sbufOR.append(saField[i]);
            sbufOR.append(",");
        }
        sbufOR.delete(sbufOR.length() - 1, sbufOR.length());

        return sbufOR.toString();
    }


    /**
     * <b> 功能 : 将数组拼成IN风格字符串 例如 String[]{"A","B","C"}  --> "in ('A','B','C')" </b>
     *
     * @param strs 字符串数组
     * @return
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getInStrFromStrings(String[] strs) {
        String in = "";
        for (String str : strs) {
            if (null != getString_TrimZeroLenAsNull(str)) {
                in += "'" + str + "',";
            }

        }

        if (null != getString_TrimZeroLenAsNull(in)) {
            in = in.substring(0, in.length() - 1);
        } else {
            in = "''";
        }
        return in;
    }

    /**
     * <b> 功能 : 替换SQL中某个 条件 </b>
     *
     * @param str    需要替换的字符串
     * @param field  替换起始位置字段
     * @param re_str 替换后的字段
     * @return 替换后的字符串
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String replaceStr(String str, String field, String value, String re_str) {
        int begin = str.indexOf(field);//开始位置
        int fh = str.substring(begin, str.length()).indexOf("'");//从开始位置往后第一个引号位置
        String temp = str.substring(begin, begin + fh + value.length() + 2);//需要替换的字符串 (从 起始位置 到 值之后的引号位置) 后面加的2为 2个引号
        str = str.replace(temp, re_str);//替换值
        return str;
    }


    /**
     * <b> 功能 : 生成INSERT语句 </b>
     *
     * @param names  字段名字
     * @param values 值
     * @param table  表名
     * @return 生成的INSERT语句
     * @作者 张帅
     * @创建日期 2011-9-8
     */
    public static String getInsertSQL(String[] names, ArrayList values, String table) {
        StringBuffer buffer = new StringBuffer("INSERT INTO " + table + " (");
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase("ts")) {
                continue;
            }
            buffer.append(names[i] + ",");
        }
        buffer.setLength(buffer.length() - 1);
        buffer.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) != null) {
                if (values.get(i).toString().equalsIgnoreCase("ts")) {
                    continue;
                }
            }
            if (values.get(i) != null) {
                buffer.append("'" + values.get(i).toString() + "',");
            } else {
                buffer.append(null + ",");
            }

        }
        buffer.setLength(buffer.length() - 1);
        buffer.append(")");
        return buffer.toString();
    }


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
     * 数据设置存储到数据库的时候加工
     *
     * @param object
     * @return
     */
    public static Object setFiledChange(Object object) {

        //对字符中的空 null等做处理
        if (object instanceof String) {
            if (null == object || "".equals(object) || "null".equals(object)) {
                return "";
            }
        }

        return object;
    }

    /**
     * 数据列从数据库取出后的时候加工
     *
     * @param object
     * @return
     */
    public static Object getFiledChange(Object object) {
        //对字符中的空 null等做处理
        if (object instanceof String) {
            if (null == object || "".equals(object) || "null".equals(object)) {
                return "";
            }
        }
        return object;
    }

    /**
     * 生成IbeaconUUID信息，根据WIFISSID生成，中间增加横杠
     *
     * @param text 32位SSID信息
     * @return
     */
    public static String genIbeaconUUID(String text) {
        text = text.substring(0, 8) + "-" + text.substring(8, 12) + "-" + text.substring(12, 16) + "-" + text.substring(16, 20) + "-" + text.substring(20);
        return text;
    }

    /**
     * <b> 功能 : 处理跨域问题 </b>
     *
     * @作者 张帅
     * @创建日期 2014年12月25日
     */
    public static String checkJsonp(String jsoncallback, String json) {
        if (null == getString_TrimZeroLenAsNull(jsoncallback)) {
            return json;
        } else {
            return jsoncallback + "(" + json + ")";
        }
    }

    /**
     * 格式化小数，只保存小数后2位
     *
     * @param value
     * @return
     */
    public static String formatDouble(Double value) {
        if ("NaN".equals(value.toString())) {
            value = 0.0;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String num = df.format(value);
        if (".00".equals(num)) {
            num = "0.00";
        }

        if (num.startsWith(".")) {
            num = "0" + num;
        }
        return num;
    }


}
