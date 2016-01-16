package com.example.administrator.bazipaipan.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * GsonUtils 将gson转化为javabean的工具类
 */
public class GsonUtils {

    private static final String TAG = GsonUtils.class.getSimpleName();

    public static Gson gson = new Gson();

    public static <T> T parserJson(Class<T> cls, String json) {
        try {
            if (gson == null)
                gson = new Gson();
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            if (gson == null)
                gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 将json，转换成List<Bean>的形式。
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> parerListBeans(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<T>>() {
            }.getType();
            list = gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
