package com.qhieco.util;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.qhieco.time.TimePeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by xujiayu on 17/11/11.
 * @author yekk modified at 12/04/17
 */
@Slf4j
public class TimeUtil {
	
	private static final long HOUR_2_MSECONDS = 60 * 60 * 1000L;
	public static final long MINUTE_2_MSECONDS = 60 * 1000L;
	private static final long DAY_2_MSECONDS = 24 * 60 * 60 * 1000L;
	private static final long WEEK_2_MSECONDS = 7 * 24 * 60 * 60 * 1000L;
	private static final int FREE_CANCELLATION_TIME = 0;
	private static final int MIN_CHARGING_PERIOD = 15;
	private static final int MIN_SHARING_PERIOD = 30;
	private static final long FIRST_NIGHT_TIMESTAMP = 57600000L;

	public static long getHour2Mseconds() {
		return HOUR_2_MSECONDS;
	}

	public static long getDay2Mseconds() {
		return DAY_2_MSECONDS;
	}

	public static long getWeek2Mseconds() {
		return WEEK_2_MSECONDS;
	}

	public static int getFreeCancellationTime() {
		return FREE_CANCELLATION_TIME;
	}

	public static int getMinChargingPeriod() {
		return MIN_CHARGING_PERIOD;
	}

	public static int getMinSharingPeriod() {
		return MIN_SHARING_PERIOD;
	}

	public static String stampToDate(Long time) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		res = simpleDateFormat.format(date);
		return res;
	}

	public static Long dateTotamp(String dateStr) {
		Long res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			res = simpleDateFormat.parse(dateStr).getTime();
			return res;
		}catch (Exception e){
			return null;
		}
	}

	public static int days(int year, int month) {
		int days = 0;
		if (month != 2) {
			switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					days = 31;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					days = 30;
                default:
                    break;
			}
		} else {
			// 闰年
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				days = 29;
			}
			else {
				days = 28;
			}
		}
		return days;
	}

	public static int getDiffDays(Long activeTime, Long now) {
		return (int) ((now - activeTime) / DAY_2_MSECONDS);
	}

	/**
	 * 单位转换, 分钟转换为毫秒
	 * @param minutes 单位为分钟
	 * @return 单位为毫秒
	 */
	public static long minutesToMilliSeconds(int minutes) {
		return minutes * 60 * 1000L;
	}

	public static int milliSecondsToMinutes(long milliSeconds) {
		return (int) milliSeconds / (60 * 1000);
	}

	/**
	 * 计算milliSeconds是多少个小时,不足一个小时按一个小时算：如61分钟，算两个小时
	 * @param milliSeconds
	 * @return
	 */
	public static int milliSecondsToHours(long milliSeconds) {
		return (int) Math.ceil(milliSeconds / (60 * 60 * 1000.0));
	}

	/**
	 * milliSeconds取余分钟
	 * @param milliSeconds
	 * @return
	 */
	public static int milliSecondsRemainderMunite(long milliSeconds) {
		return (int) (milliSeconds / (60 * 1000)) % 60;
	}

	/**
	 * 0~6分别表示周日至周六
	 * @return 周日至周六分别返回0~6
	 */
	public static int getDayOfWeekToday() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek - 1;
	}

	/**
	 * 判断两个时间是否为同一天
	 * @param cal Calendar类实例化对象
	 * @param time1	 时间1
	 * @param time2 时间2
	 * @return
	 */
	public static boolean isSameDay(Calendar cal,Long time1,Long time2){
		cal.setTimeInMillis(time1);
		int dayOfYear1 = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(time2);
		int dayOfYear2 = cal.get(Calendar.DAY_OF_YEAR);
		if(dayOfYear1 == dayOfYear2){
			return true;
		}else{
			return false;
		}
	}

    /**
     * 时间戳转化为字符串
     * @param timeStamp 时间戳
     * @return
     */
	public static String timestamp2SpecialStr(Long timeStamp) {
	    Calendar calendar = Calendar.getInstance();
	    long now = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(new Date(timeStamp));
        if(isSameDay(calendar, timeStamp, now)) {
            return "当天" + time;
        } else if(now > timeStamp){
            return "昨日" + time;
        }else{
        	return "次日"+ time;
		}
    }


	/**
	 * 时间戳转化为字符串
	 * @param timeStamp 时间戳
	 * @return
	 */
	public static String timestamp2SpecialStr2(Long timeStamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return simpleDateFormat.format(new Date(timeStamp));
	}


	/**
	 * 时间戳转化为字符串
	 * @param timeStamp 时间戳
	 * @return
	 */
	public static String timestampToStr(Long timeStamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
		return simpleDateFormat.format(new Date(timeStamp));
	}


	/**
	 * 时间戳转化为字符串年月日
	 * @param timeStamp 时间戳
	 * @return
	 */
	public static String timestampToStr2(Long timeStamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
		return simpleDateFormat.format(new Date(timeStamp));
	}


	/**
	 * 字符串转化为时间戳
	 * @param timeStr 时间字符串格式
	 * @return
	 */
	public static Long strToTimestamp(String timeStr) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			return simpleDateFormat.parse(timeStr).getTime();
		}catch (Exception e){
			return null;
		}

	}

    /**
     * 得到传入的时间是星期几
     * @param time 时间
     * @return 返回星期几（0 - 6）
     */
	public static int dayOfWeek(Long time) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(time);
	    return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

	/**
	 * 判断当天是否是工作日
	 * @param time 时间戳
	 * @return true：是工作日，false：不是工作日
	 */
	public static boolean isWeekDay(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return !(i == Calendar.SUNDAY || i == Calendar.SATURDAY);
    }

    /**
     * 构造发布时间段，将发布时间段全部映射到一天内，范围从 [-28800000 - 57600000]
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 发布时间范围
     */
    public static String makeTimeRange(long startTime, long endTime) {
        int dayOfStartTime = dayOfWeek(startTime);
        int dayOfEndTime = dayOfWeek(endTime);
		System.out.println("start time is " + formatOneDayTime(startTime));
        return dayOfStartTime == dayOfEndTime?
                String.format("%s:%s", formatOneDayTime(startTime), formatOneDayTime(endTime)):
                String.format("%s:%s#%s:%s", formatOneDayTime(startTime), DAY_2_MSECONDS + FIRST_NIGHT_TIMESTAMP,
                        FIRST_NIGHT_TIMESTAMP, formatOneDayTime(endTime));
    }

	/**
	 * 将时间映射到1970/1/2
	 * @param time 时间戳
	 * @return 1970/1/2号的时间戳
	 */
	public static long formatOneDayTime(Long time) {
        return (time - FIRST_NIGHT_TIMESTAMP) % DAY_2_MSECONDS + FIRST_NIGHT_TIMESTAMP;
    }

    public static boolean isTimePeriodInList(TimePeriod inTimePeriod, ArrayList<TimePeriod> timePeriodList, String minPublishInterval) {
        for (TimePeriod timePeriod: timePeriodList) {
            if (inTimePeriod.isTimePeriodIntervalNoMoreThanConfigMinutes(timePeriod, Integer.valueOf(minPublishInterval))) {
            	log.info("重复了，已发布的时间段 {}, 将发布的时间段 {}", timePeriod, inTimePeriod);
                return true;
            }
        }
        return false;
    }

	/**
	 * time的day天后的时间戳：前day天的时间戳，则day为负数
	 */
	public static long getTimeByDay(long time, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
		return calendar.getTimeInMillis();
	}

	/**
	 * 把时间转换成当天或者当天往后N天的日期；
	 * 例如 把2018/3/20 18:6:20 转换成 当天的18:6:20， 则n传0，time为2018/3/20 18:6:20的时间戳
	 * @param time
	 * @param n
	 * @return
	 */
	public static long changeTime2day(long time, int n){
		Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.YEAR, now.get(Calendar.YEAR));
		c.set(Calendar.MONTH, now.get(Calendar.MONTH));
		c.set(Calendar.DATE, now.get(Calendar.DATE) + n);
		return c.getTimeInMillis();
	}


	/**
	 * 把第一个时间戳转换成第二个时间戳的日期
	 * 例如 把2018/3/20 18:6:20 转换成 当天的18:6:20， 则n传0，time为2018/3/20 18:6:20的时间戳
	 * @param time
	 * @return
	 */
	public static long changeTime3day(long time, Long time2){
		Calendar c = Calendar.getInstance();
		Calendar t = Calendar.getInstance();
		c.setTimeInMillis(time);
		t.setTimeInMillis(time2);
		c.set(Calendar.YEAR, t.get(Calendar.YEAR));
		c.set(Calendar.MONTH, t.get(Calendar.MONTH));
		c.set(Calendar.DATE, t.get(Calendar.DATE));
		return c.getTimeInMillis();
	}

	/**
	 * 当前时间距离整点时间的间隔
	 * @return 时间戳, 单位分
	 */
	public static int getTimeInterval() {
		Calendar cal = Calendar.getInstance();
		// 2018年4月3日10:43:27  nowMinute为43,返回2
		int nowMinute = cal.get(Calendar.MINUTE);
		return MIN_CHARGING_PERIOD - (nowMinute % MIN_CHARGING_PERIOD);
	}

    /**
     * iso8601时间格式转化为时间戳
     * @param isoStr iso8601时间格式
     * @return 时间戳
     */
	public static Long isoStr2Timestamp(String isoStr){
	    try{
            ISO8601DateFormat df = new ISO8601DateFormat();
            Date d = df.parse(isoStr);
            return d.getTime();
        } catch (ParseException e) {
	        e.printStackTrace();
        }
        return null;
	}

    /**
     * 时间戳转化为字符串格式
     * @param timeStamp 时间戳
     * @param format 格式
     * @return 日期格式
     */
	public static String timestamp2Str(Long timeStamp, String format) {
	    if (null == timeStamp || StringUtils.isEmpty(format)) {
	        return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	    return simpleDateFormat.format(new Date(timeStamp));
    }

	public static void main(String[] args) throws ParseException{
//		final String s = makeTimeRange(1524153600000L, 1524155400000L);
//        System.out.println(s);
//
//        System.out.println(getTimeInterval());
//
//
//        System.out.println(getDayOfWeekToday());
//		Calendar calendar = Calendar.getInstance();
//		System.out.println(getTimeByDay(System.currentTimeMillis(), 3));
//
//		Long startTime = 1522512000000L;
//		long endTime = 1522598400000L;
//		// 发布时间是否跨天
//		if (TimeUtil.isSameDay(calendar, startTime, endTime)) {
//			endTime = TimeUtil.changeTime2day(endTime, 0);
//		} else {
//			endTime = TimeUtil.changeTime2day(endTime, 1);
//		}
//		System.out.println(isSameDay(calendar, startTime, endTime));
//		System.out.println(isSameDay(calendar, startTime, endTime));
//        System.out.println(isSameDay(calendar, startTime, endTime));
//		startTime = TimeUtil.changeTime2day(startTime, 0);
//        System.out.println(startTime+", " + endTime);
//
//		Calendar cal = Calendar.getInstance();
//		int nowMinute = cal.get(Calendar.MINUTE);
//		System.out.println(nowMinute);
//
//		System.out.println((TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000));
//        System.out.println(timestamp2SpecialStr(System.currentTimeMillis()));
//		final String s = makeTimeRange(1528338600000L, 1528416000000L);
		System.out.println(timeStampToHms(1528416000000L));
	}

	/**
	 * 秒转换成毫秒数
	 */
	public static long secondToMilliSeconds(int second) {
		return second * 1000L;
	}
	/**
	 * 分钟换成毫秒数
	 */
	public static long muniteToMilliSeconds(int munite) {
		return munite * 60 * 1000L;
	}


	public static String timeStamp2Date(long timeStamp, String format) {
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(timeStamp));
	}

	/**
	 * 获取用户使用车位的周期数
	 * @return 停车用户使用车位的周期数
	 */
	public static double getPeriod(long time, int minChargingPeriod) {
        double period = Math.ceil(time / (minChargingPeriod * 60 * 1000.0));
        return period;
    }

	/**
	 * 转换为时分秒格式，小时超过24后会继续增加
	 */
	public static String timeStampToHms(Long time){
		Long second = time/1000L;
		Long resSecond = second%60L;
		Long minute = second/60L;
		Long resMinute = minute%60L;
		Long hour = minute/60L;
		return String.format("%d:%d:%d",hour,resMinute,resSecond);
	}

	/**
	 * 通过时间秒毫秒数判断两个时间的间隔
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int differentDaysByMillisecond(Long time1,Long time2)
	{
		return (int) ((time2 - time1) / (1000*3600*24));
	}


	/**
	 * 获得当天零时零分零秒
	 * @return
	 */
	public static Long initDateByDay(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}
}
