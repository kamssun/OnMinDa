package com.newthread.android.util;

import com.newthread.android.bean.SingleCourseInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {
    // 获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    // 日期以月份显示
    // 2013-07-07：2013年7月
    public static String getMonthFriendly(String dateStr) {
        String date = "";
        if (dateStr.length() >= 10) {
            String[] dates = dateStr.substring(0, 10).split("-");

            if (dates[1].contains("0")) {
                dates[1] = dates[1].substring(1);
            }

            date = dates[0] + "年" + dates[1] + "月";
            return date;
        }

        return null;
    }

    // 得到今天是周几
    public static int getDayOfWeek() {
        try {
            Date now = new Date();
//			DateFormat d1 = DateFormat.getDateInstance(); // 默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16
            SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
            String str = d1.format(now);
            String[] time = str.split("-");
            int year = Integer.parseInt(time[0]);
            int month = Integer.parseInt(time[1]);
            int day = Integer.parseInt(time[2]);

            if (month == 1) {
                month = 13;
                year--;
            }
            if (month == 2) {
                month = 14;
                year--;
            }
            return (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4
                    - year / 100 + year / 400) % 7;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // 得到今天是这学期的第几周
    public static int getWeekOfSemester() {
        Date now = new Date();
//		DateFormat d1 = DateFormat.getDateInstance();
        SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
        String[] strs = d1.format(now).split("-");
        // 初始化开学时间
        int sYear = 2014;
        int sMonth = 9;
        int sDay = 2;

        int cYear = Integer.parseInt(strs[0]);
        int cMonth = Integer.parseInt(strs[1]);
        int cDay = Integer.parseInt(strs[2]);

        int weeks = 0;
        if (sYear == cYear) {
            // 开学年份与今天在同一年
            int days = getDayOfYear(cYear, cMonth, cDay)
                    - getDayOfYear(sYear, sMonth, sDay);
            int dayOfWeek = getDayOfWeek(cYear, cMonth, cDay);
            int days0 = days - dayOfWeek;
//
//				if (days % 7 != 0)
//					weeks++;

            weeks += days / 7 + 1;

        } else {
            if ((sYear % 4 == 0) && (sYear % 100 != 0) || sYear % 400 == 0) {
                int days = 366 - getDayOfYear(sYear, sMonth, sDay) + getDayOfYear(cYear, cMonth, cDay);
                weeks += days / 7 + 1;
            } else {
                int days = 365 - getDayOfYear(sYear, sMonth, sDay) + getDayOfYear(cYear, cMonth, cDay);
                weeks += days / 7 + 1;
            }
        }

        return weeks;
    }

    //得到课程表可变参数
    public static String getChangeCouserUrl() {
        Date now = new Date();
        SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
        String[] strs = d1.format(now).split("-");
        int cYear = Integer.parseInt(strs[0]);
        int cMonth = Integer.parseInt(strs[1]);
        if (cMonth > 7) {
            return cYear + "-" + (cYear + 1) + "-" + 1;
        }
        return (cYear - 1) + "-" + cYear + "-" + 2;
    }

    // 得到今天是一年中的第几天
    public static int getDayOfYear(int year, int month, int day) {
        int sum_days = 0;
        // 计算天数 巧用switch语句
        switch (month - 1) {
            case 11:
                sum_days += 30;
            case 10:
                sum_days += 31;
            case 9:
                sum_days += 30;
            case 8:
                sum_days += 31;
            case 7:
                sum_days += 31;
            case 6:
                sum_days += 30;
            case 5:
                sum_days += 31;
            case 4:
                sum_days += 30;
            case 3:
                sum_days += 31;
            case 2:
                sum_days += 28;
            case 1:
                sum_days += 31;
                break;
            default:
                break;
        }

        // 判断是否是闰年 闰年2月29天
        if ((year % 4 == 0) && (year % 100 != 0) || year % 400 == 0)
            if (month > 2)
                sum_days++;

        sum_days += day;
        return sum_days;
    }

    // 得到今天是周几
    public static int getDayOfWeek(int year, int month, int day) {

        if (month == 1) {
            month = 13;
            year--;
        }
        if (month == 2) {
            month = 14;
            year--;
        }
        return (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year
                / 100 + year / 400) % 7 + 1;
    }

    /**
     * @return 返回符合clock格式当前时间
     */
    public static long getClockTestCurrentTime() {
        Date date = new Date();
        // format对象是用来以指定的时间格式格式化时间的
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmm"); // 这里的格式可以自己设置
        // format()方法是用来格式化时间的方法
        String times = from.format(date);
        return Long.parseLong(times);
    }

    public static String getTimeFromCourse(SingleCourseInfo singleCourseInfo,int beforeMinute) {
        char firstNum = singleCourseInfo.getNumOfDay().charAt(1);
        String courseTime = null;
        switch (firstNum) {
            case '1':
                courseTime = "0800";
                break;
            case '3':
                courseTime = "1000";
                break;
            case '5':
                courseTime = "1410";
                break;
            case '7':
                courseTime = "1600";
                break;
            case '9':
                courseTime = "1840";
                break;
            default:
                break;
        }
        String dateTime = getDateTimeFromeCouser(singleCourseInfo);
        String time =dateTime + courseTime;
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            return from.format(getDateBeforeMin(from.parse(time), beforeMinute));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getDateTimeFromeCouser(SingleCourseInfo singleCourseInfo) {
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date date = cal.getTime();
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd"); // 这里的格式可以自己设置
        return from.format(getDateAfterDay(date,singleCourseInfo.getNumOfWeek()));
    }
    /**
     * 得到几天后的时间
     * @param d
     * @param day
     * @return
     */
    private static Date getDateAfterDay(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }
    /**
     * 得到几天后的时间
     * @param d 日期
     * @param min 提前的分钟
     * @return
     */
    private static Date getDateBeforeMin(Date d, int min){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmm");
        now.set(Calendar.MINUTE,now.get(Calendar.MINUTE)-min);
        return now.getTime();
    }


}
