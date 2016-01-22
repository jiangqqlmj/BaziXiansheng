package com.example.administrator.bazipaipan.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.example.administrator.bazipaipan.login.LoginContainerActivity;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.view.model.Recharge;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 王中阳 on 2015/12/31.
 */
public class BmobUtils {
    //确认注销
    public static void onCancelPressed(final Activity context) {
        new AlertDialog.Builder(context).setTitle("确认注销吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        EMChatManager.getInstance().logout();//环信注销登陆
                        BmobUser.logOut(context);   //清除缓存用户对象
                        Intent intent = new Intent(context, LoginContainerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                        context.startActivity(intent);
                        context.finish();

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    //缓存
    public static void setCache(BmobQuery<Recharge> query, Context context, BmobObject bmobObject) {
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(context, BmobObject.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//            Log.e("bmob", "CACHE_ELSE_NETWORK");
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
    }

    //intent传值
    public static int getIntExtra(Activity activity, String changliang) {

        int msg = activity.getIntent().getIntExtra(changliang, 0);
        return msg;
    }

    public static String getStringExtra(Activity activity, String string) {
        String msg = activity.getIntent().getStringExtra(string);
        return msg;
    }

    //log打印
    public static void log(String msg) {
        Log.e("datas", msg);
    }

    //获得当前对象id
    public static String getCurrentId(Context mycontext) {
        String id = BmobUser.getCurrentUser(mycontext, MyUser.class).getObjectId();
        return id;
    }


    //获得当前对象
    public static MyUser getCurrentUser(Context mycontext) {
        MyUser currentUser = BmobUser.getCurrentUser(mycontext, MyUser.class);
        return currentUser;
    }

    //更新
    public static void updateBmob(Context context, String currentuid, BmobObject bmobObject, String key, Objects value) {
        BmobObject bean = new BmobObject();
        bean.setValue(key, value);
        bean.update(context, currentuid, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("bmob", "更新成功：");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.e("bmob", "更新失败：" + msg);
            }
        });

    }

    //查询数据
//    public BmobObject bmobQuery(Context mContext, BmobObject bmobObject, String currentuid,) {
//        BmobQuery<BmobObject> query = new BmobQuery<BmobObject>();
//        BmobObject object;
//        query.getObject(mContext, currentuid, new GetListener<BmobObject>() {
//
//            @Override
//            public void onSuccess(BmobObject object) {
//                // TODO Auto-generated method stub
//                Log.e("bmob", "查询数据" + object.toString());
//            }
//
//            @Override
//            public void onFailure(int code, String arg0) {
//                // TODO Auto-generated method stub
//                Log.e("bmob", "查询失败" + arg0);
//                return;
//            }
//
//        });
//    }

    //图片相关

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
