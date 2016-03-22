package dong.lan.flextime.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public final static String FORMAT_YEAR = "yyyy";
    //public final static String FORMAT_TIME = "dd天 HH小时 mm分";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATA_TIME_SECOND = "yyyy年MM月dd日 HH:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟
    private static StringBuffer sb = new StringBuffer();
    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        String strTime = "";
        Date date = longToDate(currentTime, formatType);// long类型转成Date类型
        strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getYear() {
        return new SimpleDateFormat(FORMAT_YEAR).format(new Date());
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getRemainTime(String time) {
        long timesamp = stringToLong(time, FORMAT_DATA_TIME_SECOND);
        StringBuilder sb = new StringBuilder();
        long gap = (timesamp - System.currentTimeMillis()) / 1000;
        if (gap < 0) {
            sb.append("已超过完成期限: ");
            gap = 0 - gap;
        } else {
            sb.append("距日程结束还有: ");
        }
        int y;
        int mon;
        int d;
        int h;
        int m;
        y = (int) (gap / YEAR);
        gap = gap - YEAR * y;
        mon = (int) (gap / MONTH);
        gap = gap - mon * MONTH;
        d = (int) (gap / DAY);
        gap = gap - d * DAY;
        h = (int) (gap / HOUR);
        gap = gap - h * HOUR;
        m = (int) (gap / MINUTE);

        if (y != 0) {
            sb.append(y);
            sb.append("年 ");
        }
        if (mon != 0) {
            sb.append(mon);
            sb.append("月 ");
        }
        if (d != 0) {
            sb.append(d);
            sb.append("天 ");
        }
        sb.append(h);
        sb.append("小时 ");
        sb.append(m);
        sb.append("分");

        return sb.toString();
    }

    /**
     * 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
     *
     * @param @param  timesamp
     * @param @return
     * @return String
     * @throws
     * @Title: getChatTime
     * @Description: TODO
     */
    public static String getChatTime(long timesamp) {
        long clearTime = timesamp * 1000;
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(otherDay))
                - Integer.parseInt(sdf.format(today));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "明天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "后天 " + getHourAndMin(clearTime);
                break;

            default:
                result = getTime(clearTime);
                break;
        }

        return result;
    }

    public static long getTimeValues(String startTime, String endTime, int level) {

        return (stringToLong(endTime, FORMAT_DATA_TIME_SECOND) - stringToLong(startTime, FORMAT_DATA_TIME_SECOND) % DAY * level);
    }

    public static long getLongNeedTime(String needTime)
    {
        long time =0;
        time += Long.valueOf(needTime.substring(0,needTime.indexOf("天"))) * DAY;
        time += Long.valueOf(needTime.substring(needTime.indexOf("天")+2,needTime.indexOf("小"))) * HOUR;
        time += Long.valueOf(needTime.substring(needTime.lastIndexOf(" ")+1,needTime.indexOf("分"))) * MINUTE;
        return time;
    }
    public static long getStartTime(String needTime, String endTime) {

        return stringToLong(endTime, FORMAT_DATA_TIME_SECOND) -
                getLongNeedTime(needTime);
    }

    public static long getTimeOfTwo(String a,String b)
    {
        return stringToLong(a,FORMAT_DATA_TIME_SECOND) -stringToLong(a,FORMAT_DATA_TIME_SECOND);
    }

    public static String getStartTimeGap(String needTime, String endTime)
    {
        sb.delete(0,sb.length());
        long time = (stringToLong(endTime, FORMAT_DATA_TIME_SECOND) -
                getLongNeedTime(needTime) - System.currentTimeMillis())/1000;
        int d ;
        int h;
        int m;
        sb.append("<p>");
        sb.append("所需时间 ");
        sb.append(needTime);
        sb.append("</p>");
        sb.append("<p>");
        if(time >=0)
        {
            sb.append("距开始还有 ");
        }else
        {
            sb.append("已开始 ");
            time = 0-time;
        }
        d = (int) (time / DAY);
        time = time -d*DAY;
        h = (int) (time /HOUR);
        time = time -h*HOUR;
        m = (int) (time /MINUTE);
        if (d != 0) {
            sb.append(d);
            sb.append("天 ");
        }
        sb.append(h);
        sb.append("小时 ");
        sb.append(m);
        sb.append("分");
        sb.append("</p>");
        return  sb.toString();
    }

}