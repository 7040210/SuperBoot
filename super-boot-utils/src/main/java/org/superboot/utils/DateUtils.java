package org.superboot.utils;

import com.xiaoleilu.hutool.date.DateUtil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <b> 日期处理类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class DateUtils {


    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    /**
     * 获取当前日期 格式 YYYY-MM-DD
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(System.currentTimeMillis()) + "";
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前日期和时间 格式 YYYY-MM-DD HH:mm:ss
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis()) + "";
    }


    /**
     * 获取当前日期和时间截止到分钟 格式 YYYY-MM-DD HH:mm
     */
    public static String getCurrentDateTimeEndMM() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(System.currentTimeMillis()) + "";
    }

    /**
     * 获取当前时间 格式HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(System.currentTimeMillis()) + "";
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate 日期
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 HH:mm:ss
     *
     * @param dateDate 日期
     * @return
     */
    public static String timeToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }


    /**
     * 获取一个月的最后一天
     *
     * @param dat
     * @return
     */
    public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
                || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }

        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     *
     * @return
     */
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1) {
            week = "0" + week;
        }
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }


    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     *
     * @param st1
     * @param st2
     * @return
     */
    public static String getTwoHour(String st1, String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0])) {
            return "0";
        } else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
                    / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
                    / 60;
            if ((y - u) > 0) {
                return y - u + "";
            } else {
                return "0";
            }
        }
    }


    /**
     * 得到二个日期间的间隔天数
     *
     * @param sj1
     * @param sj2
     * @return
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 时间后推分钟.
     *
     * @param sj1 19位时间
     * @param jj  分钟数 正数为后推 负数为前推
     * @return
     */
    public static String getPreTime(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";
        try {
            Date date1 = format.parse(sj1);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {
        }
        return mydate1;
    }

    /**
     * 判断是否润年
     *
     * @param ddate
     * @return
     */
    public static boolean isLeapYear(String ddate) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 得到一个时间延后或前移几天的时间
     *
     * @param nowdate 日期
     * @param delay   delay为前移或后延的天数 正数为后推 负数为前推
     * @return
     */
    public static String getNextDay(String nowdate, String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date d = strToDate(nowdate);
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
                    * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 计算2个精度到分钟的时间的时间差，结果为分钟数
     *
     * @param date1 17位字符 如2012-02-02 13:22
     * @param date2 17位字符 如2012-02-02 13:32
     * @return
     * @throws ParseException
     */
    public static long getMinDifferNum(String date1, String date2)
            throws ParseException {
        long r = 0L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = df.parse(date1);
        Date date = df.parse(date2);
        long l = now.getTime() - date.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;

        r = day * 24L * 60L + hour * 60L + min;
        String s = String.valueOf(hour) + ":" + String.valueOf(min);
        return r;
    }

    /**
     * 计算2个精度到日期的时间差，结果为天数
     *
     * @param dates_now 十位日期 2012-02-02
     * @param dates_to  十位日期 2012-02-03
     * @return
     * @throws ParseException
     */
    public static long getDifferDays(String dates_now, String dates_to)
            throws ParseException {
        long r = 0L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = df.parse(dates_now);
        Date date = df.parse(dates_to);
        long l = now.getTime() - date.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;
        r = day;
        return r;
    }

    /**
     * 计算2个时间的中文时间差 返回结果为 XX：xx  比如 12:10
     *
     * @param dates_now
     * @param dates_to
     * @return
     * @throws ParseException
     */
    public static String getMinDiffer(String dates_now, String dates_to)
            throws ParseException {
        long r = 0L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date now = df.parse(dates_now);
        Date date = df.parse(dates_to);
        long l = now.getTime() - date.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;

        String s = String.valueOf(hour) + ":" + String.valueOf(min);
        return s;
    }

    /**
     * 计算2个时间差，返回精确到秒
     *
     * @param date_time1 当前时间
     * @param Date_time2 对比时间
     * @return
     */
    public static long getSecondDiffer(String date_time1, String Date_time2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(date_time1);
            Date d2 = df.parse(Date_time2);
            long diff = d1.getTime() - d2.getTime();
            return diff / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据日期获取中文星期
     *
     * @param date 10位日期字符
     * @return
     */
    public static String getCnWeekDay(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int number = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            String[] str = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            return str[number];
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据日期获取中文星期
     *
     * @param Num 1-7 周一到周日
     * @return
     */
    public static String getCnWeekByNum(int Num) {
        String[] str = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return str[Num];
    }

    /**
     * 根据日期获取中文星期
     *
     * @param WeekName 周一到周日
     * @return
     */
    public static int getNumByCnWeek(String WeekName) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("周一", 1);
        map.put("周二", 2);
        map.put("周三", 3);
        map.put("周四", 4);
        map.put("周五", 5);
        map.put("周六", 6);
        map.put("周日", 7);
        return map.get(WeekName);
    }

    /**
     * 根据传入日期判断是当年的第几周
     *
     * @param date 日期 格式为 2015-01-01
     * @return
     */
    public static int getWeekByDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar ca = Calendar.getInstance();
            //设置星期一为一周的第一天。
            ca.setFirstDayOfWeek(Calendar.MONDAY);
            ca.setTime(d);
            //当年的第几天
            ca.get(Calendar.DAY_OF_YEAR);
            //当年的第几周
            ca.get(Calendar.WEEK_OF_YEAR);
            //当月的第几天
            ca.get(Calendar.DAY_OF_MONTH);
            //本周的第几天 因为默认第一天是周日 所以得到的日期要减一
            ca.get(Calendar.DAY_OF_WEEK);
            //本月的第几周
            ca.get(Calendar.WEEK_OF_MONTH);

            return ca.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取今天为第几周
     *
     * @return
     */
    public static int getWeekByToday() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(getCurrentDate());
            Calendar ca = Calendar.getInstance();
            //设置星期一为一周的第一天。
            ca.setFirstDayOfWeek(Calendar.MONDAY);
            ca.setTime(d);
            //当年的第几天
            ca.get(Calendar.DAY_OF_YEAR);
            //当年的第几周
            ca.get(Calendar.WEEK_OF_YEAR);
            //当月的第几天
            ca.get(Calendar.DAY_OF_MONTH);
            //本周的第几天 因为默认第一天是周日 所以得到的日期要减一
            ca.get(Calendar.DAY_OF_WEEK);
            //本月的第几周
            ca.get(Calendar.WEEK_OF_MONTH);

            return ca.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static String getCurrentYear() {
        return getCurrentDate().substring(0, 4);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static String getCurrentMonth() {
        return getCurrentDate().substring(5, 7);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int week) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        //设置星期一为一周的第一天。
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);
        return getFirstDayOfWeek(sdf.format(cal.getTime()));
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int week) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        //设置星期一为一周的第一天。
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(sdf.format(cal.getTime()));
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return sdf.format(calendar.getTime());
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return sdf.format(calendar.getTime());
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfLastWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getLastDayOfWeek(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(Integer year, Integer month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(Integer year, Integer month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfLastMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfQuarter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfQuarter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getLastDayOfQuarter(Integer year, Integer quarter) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfLastQuarter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }


    /**
     * 格式化日期用于前端显示 几分钟前、几小时前、几天前等。
     *
     * @param date 19位年月日时分秒 2012-01-02 01:02:03
     * @return
     */
    public static String formatShow(String date) {
        if (10 == date.length()) {
            date += " 00:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delta = new Date().getTime() - calendar.getTime().getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    /**
     * 获取当月日期列表
     *
     * @param d 日期
     * @return
     */
    public static List<String> getMonList(Date d, String fmt) {
        if (null == fmt) {
            fmt = "dd";
        }
        List<String> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH);
        while (cal.get(Calendar.MONTH) == month) {
            list.add(DateUtil.format(cal.getTime(), fmt));
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }

    /**
     * 获取当月日期列表
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static List<String> getMonList(String date, String fmt) {
        return getMonList(DateUtil.parse(date), fmt);
    }

    /**
     * 获取当月日期列表
     *
     * @param date
     * @return
     */
    public static List<String> getMonList(String date) {
        return getMonList(DateUtil.parse(date), "dd");
    }


    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }


    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static void main(String[] args) {
        //获取当前是第几周
        int week = DateUtils.getWeekByToday();
        //当前日期
        String CurrentDate = DateUtils.getCurrentDate();

        //获取本周第一天
        String cBegin_date = DateUtils.getFirstDayOfWeek(CurrentDate);
        //获取本周最后一天
        String cEnd_date = DateUtils.getLastDayOfWeek(CurrentDate);
        //获取上周第一天
        String lBegin_date = DateUtils.getFirstDayOfWeek(Integer.valueOf(DateUtils.getCurrentYear()), week - 1);
        //获取上周最后一天
        String lEnd_date = DateUtils.getLastDayOfWeek(lBegin_date);
        //获取本月第一天
        String cMonthFirstDate = DateUtils.getFirstDayOfMonth(Integer.valueOf(DateUtils.getCurrentYear()), Integer.valueOf(DateUtils.getCurrentMonth()) - 1);
        //获取本月最后一天
        String cMonthLastDate = DateUtils.getLastDayOfMonth(Integer.valueOf(DateUtils.getCurrentYear()), Integer.valueOf(DateUtils.getCurrentMonth()) - 1);


        //获取上月第一天
        String lMonthFirstDate = DateUtils.getFirstDayOfMonth(Integer.valueOf(DateUtils.getCurrentYear()), Integer.valueOf(DateUtils.getCurrentMonth()) - 2);
        //获取上月最后一天
        String lMonthLastDate = DateUtils.getLastDayOfMonth(Integer.valueOf(DateUtils.getCurrentYear()), Integer.valueOf(DateUtils.getCurrentMonth()) - 2);


        //根据日期判断是当年第几周
        System.out.println(getWeekByDate("2016-04-13"));

        //获取某年的某周的第一天
        System.out.println(getFirstDayOfWeek(2016, 16));
        //获取某年的某周的最后一天
        System.out.println(getLastDayOfWeek(2016, 16));

        System.out.println(getTwoDay("2016-05-01", "2016-04-30"));

        System.out.println(getPreTime("2014-04-13 08:00:00", "30").substring(11, 16));


        try {
            System.out.println(getDifferDays("2014-04-02", "2014-04-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(DateUtils.getWeekByDate("2016-11-03") - DateUtils.getWeekByDate("2016-09-05") + 1);
        System.out.println("key" + System.currentTimeMillis());
    }

    /**
     * 计算当月最后一天,返回字符串
     *
     * @return
     */
    public String getEndDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }
}
