package school.lg.overseas.school.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {

    /**
     * 格式化日期，由old格式向new格式转化
     */
    public static String formatDate(String date, String oldFormat, String newFormat) {
        long longTime = stringToLong(date, oldFormat);
        return longToString(longTime, newFormat);
    }

    public static long ONE_DAY = 24 * 60 * 60 * 1000;

    public static int dateLess(long oldTime, long nowTime) {
        long less = nowTime - oldTime;
        return (int) (less / ONE_DAY);
    }

    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }


    private static SimpleDateFormat getDateFormat(String formatType) {
        return new SimpleDateFormat(formatType, Locale.CHINA);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    public static Date stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = getDateFormat(formatType);
            return formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    // formatType格式为yyyy-MM-dd HH:mm:ss ==> yyyy年MM月dd日 HH时mm分ss秒
    public static String dateToString(Date data, String formatType) {
        return getDateFormat(formatType).format(data);
    }

    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType);
        return dateToString(date, formatType);
    }

    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime);
        String sDateTime = dateToString(dateOld, formatType);
        return stringToDate(sDateTime, formatType);
    }

    /**
     * 返回分钟数
     */
    public static String getSafeTimeNumStr(int num) {
        if (num > 9) {
            return String.valueOf(num);
        } else {
            return "0" + String.valueOf(num);
        }
    }
}