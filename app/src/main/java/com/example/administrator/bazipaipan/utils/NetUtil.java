package com.example.administrator.bazipaipan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.administrator.bazipaipan.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by 王中阳 on 2015/7/7.
 */
public class NetUtil {
    public static final String TAG = "NetUtil";

    /**
     * 检查网络状态
     */
    public static boolean CheckNetState() {
        android.net.ConnectivityManager cManager = (android.net.ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取连接网络的对象
     *
     * @return
     */
    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager connectMgr = (ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        return info;
    }

    /**
     * 判断是否是手机网络
     *
     * @return
     */
    public static boolean isMobile() {
        boolean flag = false;
        NetworkInfo info = getNetworkInfo();
        // 非wifi网络
        if (info != null) {
            if (!info.getTypeName().equals("WIFI")) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断是否是WIFI
     *
     * @return
     */
    public static boolean isWifi() {
        boolean flag = false;
        NetworkInfo info = getNetworkInfo();
        // 非wifi网络
        if (info != null) {
            if (info.getTypeName().equals("WIFI")) {
                flag = true;
            }
        }
        return flag;
    }


    /**
     * ping 获取域名对应ip地址
     *
     * @param IP
     * @return
     */
    public static String PingHost(String m_strForNetAddress) {
        String result = "";
        Process p;
        try {
            p = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + m_strForNetAddress);
            int status = p.waitFor();

            if (status == 0) {
                result = "success";
            } else {
                result = "failed";
            }
            String ipStr = new String();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String str = new String();
            while ((str = buf.readLine()) != null) {
                if (str.startsWith("PING")) {
                    ipStr = str;
                    break;
                }
            }
            String[] strArr = str.split("\\(|\\)");
            if (strArr.length == 5) {
                return strArr[1];
            }
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        return "223.202.49.186";

    }

    /**
     * 获取当然网络连接类型
     *
     * @return
     */
    public static String getNetWorkType() {
        String netStatue = "";
        android.net.ConnectivityManager connManager = (android.net.ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String extraInfo = info.getExtraInfo();
            if (extraInfo != null && extraInfo.contains("wap")) {
                netStatue = "2g";
            } else {
                if (PhoneHelper.isNetWorkType3G()) {
                    netStatue = "3g";
                } else if (PhoneHelper.isNetWorkType4G()) {
                    netStatue = "4g";
                } else {
                    netStatue = "wifi";
                }
            }
        }

        return netStatue;
    }
}
