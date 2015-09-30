package com.qian.zhihuribao.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式转换，处理工具
 */
public class DateUtil {

    public static final String FORMAT_1 = "yyyy";
    public static final String FORMAT_2 = "yyyy-MM";
    public static final String FORMAT_3 = "yyyy-MM-dd";
    public static final String FORMAT_4 = "yyyy-MM-dd HH";
    public static final String FORMAT_5 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_6 = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG = DateUtil.class.getSimpleName();

    /**
     * 按照指定的格式，将日期类型对象转换成字符串，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss 如果传入的日期为null,则返回空值
     *
     * @param date   日期类型对象
     * @param format 需转换的格式
     * @return 日期格式字符串
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
     * 将日期类型对象转换成yyyy-MM-dd类型字符串 如果传入的日期为null,则返回空值
     *
     * @param date 日期类型对象
     * @return 日期格式字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(date);
    }

    /**
     * 将日期类型对象转换成yyyy-MM-dd HH:mm:ss类型字符串  如果传入的日期为null,则返回空值
     *
     * @param date 日期类型对象
     * @return 日期格式字符串
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(date);
    }

    public static String getDateCNStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return StringUtils.appendZero(cal.get(Calendar.MONTH) + 1) + "月" + StringUtils.appendZero(cal.get(Calendar.DAY_OF_MONTH)) + "日 " + getChineseWeekDay(cal.get(Calendar.DAY_OF_WEEK));
    }

    public static String getChineseWeekDay(int weekDay) {
        String showDate = "";
        switch (weekDay) {
            case 1:
                showDate = "星期日";
                break;
            case 2:
                showDate = "星期一";
                break;
            case 3:
                showDate = "星期二";
                break;
            case 4:
                showDate = "星期三";
                break;
            case 5:
                showDate = "星期四";
                break;
            case 6:
                showDate = "星期五";
                break;
            case 7:
                showDate = "星期六";
        }

        return showDate;
    }

    /**
     * 按照指定的格式，将字符串解析成日期类型对象，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     *
     * @param dateStr 日期格式的字符串
     * @param format  字符串的格式
     * @return 日期类型对象
     */
    public static Date parseDate(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将字符串（yyyy-MM-dd）解析成日期
     *
     * @param dateStr 日期格式的字符串
     * @return 日期类型对象
     */
    public static Date parseDate(String dateStr) {
        if (dateStr.indexOf("/") != -1) {
            dateStr = dateStr.replaceAll("/", "-");
        }
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    public static Date parseTime(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return new SimpleDateFormat(FORMAT_6).parse(dateStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串解析成对应日期格式的日期
     *
     * @param value 日期格式字符串
     * @return 日期类型对象
     */
    public static Date parse(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim().replaceAll("/", "-");
        if (value.length() == FORMAT_1.length()) {
            return parseDate(value, FORMAT_1);
        } else if (value.length() == FORMAT_2.length()) {
            return parseDate(value, FORMAT_2);
        } else if (value.length() == FORMAT_3.length()) {
            return parseDate(value, FORMAT_3);
        } else if (value.length() == FORMAT_4.length()) {
            return parseDate(value, FORMAT_4);
        } else if (value.length() == FORMAT_5.length()) {
            return parseDate(value, FORMAT_5);
        } else if (value.length() == FORMAT_6.length()) {
            return parseDate(value, FORMAT_6);
        } else {
            throw new RuntimeException("解析日期格式出错，与指定格式不匹配.");
        }
    }


}
