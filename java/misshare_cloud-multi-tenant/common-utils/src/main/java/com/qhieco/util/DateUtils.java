package com.qhieco.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 13:09
 * <p>
 * 类说明：
 * 时间工具类
 */
public class DateUtils {

    /**
     * 根据毫秒转化成日期
     * @param time
     * @return
     */
    public  static Date timeConvertDate(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        date2.setTime(time);
        return date2;
    }

    /**
     * 根据毫秒转化成日期
     * @param time
     * @return
     */
    public  static String timeConvertDateString(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        date2.setTime(time);
        return simpleDateFormat.format(date2);
    }


    /**
     * 获取时间段的所有日期
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd)
    {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        /**
         * 使用给定的 Date 设置此 Calendar 的时间
         */
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        /**
         * 使用给定的 Date 设置此 Calendar 的时间
         */
        calEnd.setTime(dEnd);
        /**
         * 测试此日期是否在指定日期之后
         */
        while (dEnd.after(calBegin.getTime()))
        {
            /**
             * 根据日历的规则，为给定的日历字段添加或减去指定的时间量
             */
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static void findMoth(Date dBgin,Date dEnd){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");//定义起始日期
//
//        Calendar dd = Calendar.getInstance();//定义日期实例
//
//        simpleDateFormat.parse(simpleDateFormat.format(dBgin));
//
//        dd.setTime(d1);//设置日期起始时间
//
//        while(dd.getTime().before(d2)){//判断是否到结束日期
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//
//            String str = sdf.format(dd.getTime());
//
//            System.out.println(str);//输出日期结果
//
//            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 根据毫秒转化成日期
     * @param time
     * @return
     */
    public  static String timeConvertDateStrings(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date2 = new Date();
        date2.setTime(time);
        return simpleDateFormat.format(date2);
    }

    public static long dateStrConvertTimestamp(String date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 计算相隔天数
     * @param start
     * @param end
     * @return
     */
    public static int getDaysDifference (Long start, Long end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(start));

        //日期start 在本年中的第几天
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(new Date(end));
        //日期end 在本年中的第几天
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }

    /**
     *计算相隔天数
     * @param start
     * @param end
     * @return
     */
    public static int getDays(Long start, Long end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date starts = new Date(start);
        Date ends = new Date(end);
        try {
            start = df.parse(df.format(new Date(start))).getTime();
            end = df.parse(df.format(new Date(end))).getTime();
            return Math.abs((int)(start - end)/24/3600000);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 计算相隔天数
     * @param start
     * @param end
     * @return
     */
    public static int funtion(long start, Long end){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date(end*1000));

        long n1 = c1.getTimeInMillis();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(start*1000));
        int day = c2.get(Calendar.DATE);
        int month = c2.get(Calendar.MONTH);
        int year = c2.get(Calendar.YEAR);
        c2.set(year,month,day);

        long n2 = c2.getTimeInMillis();
        System.out.println( "相差天数为:" + Math.abs((n1 - n2)/24/3600000/1000));
        return (int)Math.abs((n1 - n2)/24/3600000/1000);
    }

    /**
     * 计算相隔天数
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static int differentDays(Long date1, Long date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

}
