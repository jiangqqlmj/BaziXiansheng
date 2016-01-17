package com.example.administrator.bazipaipan;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/14.
 */
public class BaseActivity extends AutoLayoutActivity {
    //测试
    public static String BMOB_TAG = "bmob";
    //传值+fragment跳转
    public static final String PAGETO = "PageTo";
    public String mPageTo = "";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.inject(this);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(BMOB_TAG, msg);
    }

    //
    public void log(String msg) {
        Log.i("datas", msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
//        HXSDKHelper.getInstance().getNotifier().reset();
    }
}
