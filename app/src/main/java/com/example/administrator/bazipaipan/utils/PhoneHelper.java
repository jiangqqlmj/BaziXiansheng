package com.example.administrator.bazipaipan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.administrator.bazipaipan.MyApplication;

import java.util.UUID;


/**
 * Created by 春雨 on 2015/7/7.
 */
public class PhoneHelper {
    public static final String PREF_UNIQUE_ID = "pref_unique_id";
    public static final String NETTYPE_4G = "4g";
    public static final String NETTYPE_3G = "3g";
    public static final String NETTYPE_2G = "2g";
    public static final String NETTYPE_WIFI = "wifi";
    public static final String NETTYPE_UNKNOWN = "unknow";

    /**
     * 获取手机信息 手机屏幕尺寸+设备软件版本+系统版本+运行商+网络类型+用户电话号（如有卡）+手机型号 /** 获取网络说明
     * NETWORK_TYPE_UNKNOWN NETWORK_TYPE_GPRS NETWORK_TYPE_EDGE
     * NETWORK_TYPE_UMTS NETWORK_TYPE_HSDPA NETWORK_TYPE_HSUPA NETWORK_TYPE_HSPA
     * NETWORK_TYPE_CDMA NETWORK_TYPE_EVDO_0 NETWORK_TYPE_EVDO_A
     * NETWORK_TYPE_EVDO_B NETWORK_TYPE_1xRTT
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     **
     * //解释： IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
     * IMSI共有15位，其结构如下： MCC+MNC+MIN MCC：Mobile Country Code，移动国家码，共3位，中国为460;
     * MNC:Mobile NetworkCode，移动网络码，共2位 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
     * 合起来就是（也是Android手机中APN配置文件中的代码）： 中国移动：46000 46002 中国联通：46001 中国电信：46003
     * 举例，一个典型的IMSI号码为460030912121001
     *
     * IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
     * IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的 其组成为： 1.
     * 前6位数(TAC)是”型号核准号码”，一般代表机型 2. 接着的2位数(FAC)是”最后装配号”，一般代表产地 3.
     * 之后的6位数(SNR)是”串号”，一般代表生产顺序号 4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用
     * 格式[LCD:480*800][systemversion
     * :2.2][SPN:中国联通][networktype:3][phonenumber:14520100084][HTC
     * Desire][IMEI:356409048703576
     * ][MANUFACTURER:HTC][CPU_ABI:armeabi-v7a][VERSION_CODES
     * :1][PRODUCT:htc_bravo]
     * [LCD:240*320][systemversion:2.1-update1][SPN:][networktype
     * :0][phonenumber:
     * null][C8500][IMEI:a000002d0af1b8][MANUFACTURER:HUAWEI][CPU_ABI
     * :armeabi][VERSION_CODES:1][PRODUCT:C8500]
     *
     * @return
     */

    /**
     * 检查网络状态
     */
    // public static boolean CheckNetState() {
    // android.net.ConnectivityManager cManager =
    // (android.net.ConnectivityManager) MyApplication
    // .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    // android.net.NetworkInfo info = cManager.getActiveNetworkInfo();
    // if (info != null && info.isAvailable()) {
    // return true;
    // } else {
    // return false;
    // }
    //
    // }

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

    public static TelephonyManager getTelephonyManager() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm;
    }

    /**
     * 获取网络名称
     *
     * @return
     */
    public static String getNetWorkMode() {
        String netMode = NETTYPE_UNKNOWN;
        if (NetUtil.isWifi()) {
            netMode = NETTYPE_WIFI;
        } else if (isNetWorkType3G()) {
            netMode = NETTYPE_3G;
        } else if (isNetWorkType4G()) {
            netMode = NETTYPE_4G;
        } else if (isNetWorkType2G()) {
            netMode = NETTYPE_2G;
        }
        return netMode;
    }

    /**
     * 判断当前网络未知
     *
     * @return
     */
    // public static boolean isNetWorkTypeUnknown() {
    // boolean isUnknown = false;
    // ConnectivityManager connectMgr = (ConnectivityManager) MyApplication
    // .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    // NetworkInfo info = connectMgr.getActiveNetworkInfo();
    // if (info != null && info.isAvailable()) {
    // isUnknown = info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN;
    // }
    // return isUnknown;
    // }

    /**
     * 判断当前网络4G
     *
     * @return
     */
    public static boolean isNetWorkType4G() {
        boolean is4G = false;
        ConnectivityManager connectMgr = (ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            is4G = info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
        }
        return is4G;
    }

    /**
     * 判断网络是否是2G
     *
     * @return
     */
    public static boolean isNetWorkType2G() {
        boolean is2G = false;
        NetworkInfo info = getNetworkInfo();
        if (info != null && info.isAvailable()) {
            int subNetType = info.getSubtype();
            switch (subNetType) {
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    is2G = true;
                    break;
                default:
                    break;
            }

        }
        return is2G;
    }

    /**
     * 判断是否是3G
     *
     * @return
     */
    public static boolean isNetWorkType3G() {
        boolean is3G = false;
        NetworkInfo info = getNetworkInfo();
        if (info != null && info.isAvailable()) {
            int subNetType = info.getSubtype();
            switch (subNetType) {
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    is3G = true;
                    break;
                default:
                    break;
            }
        }
        return is3G;
    }

    /**
     * GPRS 2G(2.5) General Packet Radia Service 114kbps EDGE 2G(2.75G) Enhanced
     * Data Rate for GSM Evolution 384kbps UMTS 3G WCDMA 联通3G Universal Mobile
     * Telecommunication System 完整的3G移动通信技术标准 CDMA 2G 电信 Code Division Multiple
     * Access 码分多址 EVDO_0 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only
     * (Data Optimized) 153.6kps - 2.4mbps 属于3G EVDO_A 3G 1.8mbps - 3.1mbps
     * 属于3G过渡，3.5G 1xRTT 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡, HSDPA
     * 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps HSUPA
     * 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps HSPA 3G
     * (分HSDPA,HSUPA) High Speed Packet Access IDEN 2G Integrated Dispatch
     * Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科） EVDO_B 3G EV-DO Rev.B 14.7Mbps
     * 下行 3.5G LTE 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE
     * Advanced 才是4G EHRPD 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data
     * HRPD的升级 HSPAP 3G HSPAP 比 HSDPA 快些
     *
     * @param type
     * @return
     */

    public static String getNetWorkName(int type) {
        String typeName = "NETWORK_TYPE_UNKNOWN";
        // TelephonyManager.NETWORK_TYPE_GPRS;

        switch (type) {
            case 0:
                typeName = "NETWORK_TYPE_UNKNOWN";
                break;
            case 1:
                typeName = "NETWORK_TYPE_GPRS";
                break;
            case 2:
                typeName = "NETWORK_TYPE_EDGE";
                break;
            case 3:
                typeName = "NETWORK_TYPE_UMTS";
                break;
            case 4:
                typeName = "NETWORK_TYPE_CDMA";
                break;
            case 5:
                typeName = "NETWORK_TYPE_EVDO_0";
                break;
            case 6:
                typeName = "NETWORK_TYPE_EVDO_A";
                break;
            case 7:
                typeName = "NETWORK_TYPE_1xRTT";
                break;
            case 8:
                typeName = "NETWORK_TYPE_HSDPA";
                break;
            case 9:
                typeName = "NETWORK_TYPE_HSUPA";
                break;
            case 10:
                typeName = "NETWORK_TYPE_HSPA";
                break;
            case 11:
                typeName = "NETWORK_TYPE_IDEN";
                break;
            case 12:
                typeName = "NETWORK_TYPE_EVDO_B";
                break;
            case 13:
                typeName = "NETWORK_TYPE_LTE";
                break;
            case 14:
                typeName = "NETWORK_TYPE_EHRPD";
                break;
            case 15:
                typeName = "NETWORK_TYPE_HSPAP";
                break;
            default:
                typeName = "NETWORK_TYPE_UNKNOWN";
                break;

        }
        return typeName;
    }

    /**
     * 获取网络运营商识别号
     *
     * @return
     */
    public static int getNetProvider() {
        ConnectivityManager cm = (android.net.ConnectivityManager) MyApplication.getInstance()
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            if (cm.getActiveNetworkInfo().getTypeName().equals("WIFI")) {
                return 0;
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getInstance()
                        .getContext().getSystemService(
                                Context.TELEPHONY_SERVICE);
                if (telephonyManager.getSubscriberId() != null) {
                    String IMSI = telephonyManager.getSubscriberId();
                    if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                            || IMSI.startsWith("46007")) {
                        return 7012;
                    } else if (IMSI.startsWith("46001")) {
                        // ProvidersName ="中国联通";
                        return 70123;
                    } else if (IMSI.startsWith("46003")) {
                        return 70121;
                    }
                } else {
                    return -1;
                }
            }
        } else {
            return -1;
        }
        return -1;
    }

    public static String getIMEI() {

        TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null || imei.equals("") || imei.equals("0")
                || imei.startsWith("111111")) {
            imei = getUniqueId(MyApplication.getInstance().getContext());
        }
        return imei;

    }

    /**
     * 校验mac地址的合法性,支持-和：分割校验
     *
     * @author xujun
     * @return mac地址合法返回mac地址，不合法返回“”
     * */
    public static String getMACAddress() {
        WifiManager wifi = (WifiManager) MyApplication.getInstance().getContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();

            String mac = "";
            if (info != null && info.getMacAddress() != null) {
                mac = info.getMacAddress().trim();
            }

            // 正则校验MAC合法性
            String patternMac1 = "^[A-F0-9]{2}(-[A-F0-9]{2}){5}$"; // mac地址以-分割
            String patternMac2 = "^([0-9a-fA-F]{2})(([s:][0-9a-fA-F]{2}){5})$"; // mac地址以：分割
            if (TextUtils.isEmpty(mac)) {
                return "";
            }
            if (mac.matches(patternMac1)) {
                return mac;
            } else if (mac.matches(patternMac2)) {
                return mac;
            }
        }
        // 正则表达式不匹配
        return "";
    }

    private static String mDeviceToken;

    // public static void setDeviceToken(String strDeviceToken) {
    // mDeviceToken = strDeviceToken;
    // }

    // public static String getDeviceToken() {
    // return mDeviceToken;
    // }

    public static String getUniqueId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString().replace('-', '_');
            Editor editor = sharedPrefs.edit();
            editor.putString(PREF_UNIQUE_ID, uniqueID);
            editor.commit();
        }
        return uniqueID;
    }

    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getDeviceMode() {
        return android.os.Build.MODEL;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}
