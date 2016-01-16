package com.example.administrator.bazipaipan.utils;

/**
 * Created by 王中阳 on 2016/1/4.
 */
public class MyStringUtils {
    public static String cutout(String i) {
        String myString = String.valueOf(i).replace(".0", "");
        return myString;
    }

    public static String cutout(int i) {
        String myString = String.valueOf(i).replace(".0", "");
        return myString;
    }

    public static String cutout(float i) {
        String myString = String.valueOf(i).replace(".0", "");
        return myString;
    }
}
