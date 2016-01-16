package com.example.administrator.bazipaipan.utils;

import android.content.Context;

import com.example.administrator.bazipaipan.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mac on 15/9/30.
 */
public class TimeUtil {


    public static String getCurrutYearMonthDay(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.time_format));
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrutData(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.time_format_search));
        return simpleDateFormat.format(new Date());
    }

}
