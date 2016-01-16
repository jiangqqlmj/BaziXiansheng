package com.example.administrator.bazipaipan.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mac on 15/9/23.
 */
public class MixHelper {

    public static String getVersionName(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode(Context ctx, String packageName) {
        if (null == packageName || packageName.length() <= 0) {
            return -1;
        }

        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(18[0,0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    public static boolean isCarNo(String plant) {
//        Pattern pattern = Pattern.compile("^(x[\4e00-\9fa5]|a-zA-Z]{1}[s]{0,1}[0-9a-zA-Z]{5,7}$");
//        Pattern pattern = Pattern.compile("^[x{4e00}-x{9fa5}|a-zA-Z]{1}[s]{0,1}[0-9a-zA-Z]{5,7}$");
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5|A-Z]{1}[A-Z]{1}[A-Z_0-9]{5}");
        Matcher m = pattern.matcher(plant);
        return m.matches();
    }


    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + ChString.Kilometer;
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + ChString.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + ChString.Meter;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    private static Date getDate(String time) {
        Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getYearDayHour(String time) {

        String dateStr = "";
        Date date = getDate(time);
        //format的格式可以任意
        DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String getTime(String time) {
        String dateStr = "";
        Date date = getDate(time);
        //format的格式可以任意
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    private static Date getDate2(String time) {
        Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getStayTime(String time) {
        Date now = new Date();
        Date date = getDate2(time);

        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String stayTime = "";
        if (day != 0) {
            stayTime += day + "天";
        }
        if (hour != 0) {
            stayTime += hour + "小时";
        }
        if (min != 0) {
            stayTime += min + "分";
        }
//        if (s != 0) {
//            stayTime += s + "秒";
//        }
        return stayTime;
    }

    public static String getStayTime2(String time) {
        Date now = new Date();
        Date date = getDate2(time);

        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String stayTime = "";
        if (day != 0) {
            stayTime += day + "天";
        }
        if (hour > 0) {
            if (hour < 10) {
                stayTime += "0"+hour + ":";
            } else {
                stayTime += hour + ":";
            }
        } else {
            stayTime +=  "00:";
        }
        if (min != 0) {
            if (min < 10) {
                stayTime += "0" + min + "";
            } else {
                stayTime += min + "";
            }
        } else {
            stayTime += "00";
        }
//        if (s != 0) {
//            if (s < 10) {
//                stayTime += "0" + s;
//            } else {
//                stayTime += s + "";
//            }
//        } else {
//            stayTime += "00";
//        }
        return stayTime;
    }
    public static String getStayTimeFinished(String start, String end) {
        Date now = getDate2(start);
        Date date = getDate2(end);

        long l = date.getTime() - now.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String stayTime = "";
        if (day != 0) {
            stayTime += day + "天";
        }
        if (hour < 10) {
            stayTime += "0"+hour + ":";
        } else {
            stayTime += hour + ":";

        }
        if (min != 0) {
            if (min < 10) {
                if (min == 0) {
                    stayTime += "01";
                } else {
                    stayTime += "0" + min + "";
                }
            } else {
                stayTime += min + "";
            }
        } else {
            stayTime += "00";
        }
//        if (s != 0) {
//            if (s < 10) {
//                stayTime += "0" + s;
//            } else {
//                stayTime += s + "";
//            }
//        } else {
//            stayTime += "00";
//        }
        return stayTime;
    }

    public static String getStayTimeFormat(String time) {
        String str;
        if (time.contains("天")) {
            str = time.replace("天", " ").replace("小时", ":").replace("分钟", ":").replace("秒", "");
        } else if (time.contains("小时")) {
            str = time.replace("小时", "'").replace("分钟", "'").replace("秒", "''");
        } else if (time.contains("分钟")) {
            str = time.replace("分钟", "'").replace("秒", "''");
        } else {
            str = time.replace("秒", "''");
        }
        return str;
    }

}
