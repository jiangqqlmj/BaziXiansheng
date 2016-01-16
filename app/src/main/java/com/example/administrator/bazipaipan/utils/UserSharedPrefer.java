package com.example.administrator.bazipaipan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharedpreference 用户信息缓存
 */
public class UserSharedPrefer {
    public static final String XJKT_SHAREDPREFER = "onlinepark_preferences";
    //用户登录信息
    public static final String PHONENUMBER = "phonenumber";
    public static final String TOKEN = "token";
    public static final String HASREGISTRATIONID = "hasregistrationid";
    public static final String USERID = "userid";
    public static final String SERVICEISBINDED = "serviceisbinded";
    //用户真实信息
    public static final String USER_NAME = "user_name";

    public static final String FIRST_START = "first_start";

    public static void WritePreference(Context context, String key, String value) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        prefer.edit().putString(key, value).commit();
    }

    public static void WritePreference(Context context, String key, boolean value) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        prefer.edit().putBoolean(key, value).commit();
    }

    public static void WritePreference(Context context, String key, int value) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        prefer.edit().putInt(key, value).commit();
    }

    public static void RemoveVaule(Context context, String key) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        prefer.edit().remove(key).commit();
    }

    public static void RemoveAll(Context context) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        prefer.edit().clear().commit();
    }

    public static String ReadPreference(Context context, String key, String defValue) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);

        return prefer.getString(key, defValue);
    }

    public static boolean ReadPreference(Context context, String key, boolean defValue) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        return prefer.getBoolean(key, defValue);
    }

    public static int ReadPreference(Context context, String key, int defValue) {
        SharedPreferences prefer = context.getSharedPreferences(
                XJKT_SHAREDPREFER, Context.MODE_PRIVATE);
        return prefer.getInt(key, defValue);
    }
}
