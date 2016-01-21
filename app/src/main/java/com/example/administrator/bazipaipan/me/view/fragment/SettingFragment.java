package com.example.administrator.bazipaipan.me.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.BmobPro;
import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.activity.UpdateVersionActivity;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.example.administrator.bazipaipan.utils.DataCleanManager;

import java.io.File;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class SettingFragment extends Fragment implements View.OnClickListener, com.example.administrator.bazipaipan.widget.OnToggleStateChangeListener {
    public static final String TAG = "SettingFragment";


    //---------------1.8----仿iPhone开关---------
    private CheckBox push_checkbox;
    private MeContainerActivity mycontext;
    // 缓存大小
    private TextView cache_size_tv;
    private RelativeLayout cache_container, container_logout;
    String cache_size = null;
    String formatSize;
    // 工具类
    private DataCleanManager dataclean;
    // 版本更新
    private RelativeLayout update_container, container_about, container_suggestion;

    //单例
    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mycontext = (MeContainerActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        // 获取当前缓存
        get_cache_size();
    }

    private void get_cache_size() {
        // 获得当前缓存大小
        File file = new File("/data/data/com.example.administrator.bazipaipan/files");
        // TODO Auto-generated method stub
        try {
//            cache_size = dataclean.getCacheSize(file);
            //bmob的方法
            String cacheSize = String.valueOf(BmobPro.getInstance(mycontext).getCacheFileSize());
            //对文件大小进行格式化，转化为'B'、'K'、'M'、'G'等单位
            formatSize = BmobPro.getInstance(mycontext).getCacheFormatSize();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cache_size_tv.setText(formatSize + "");

    }

    private void initviews() {
        //推送按钮
        push_checkbox = (CheckBox) mycontext.findViewById(R.id.push_checkbox);

        cache_size_tv = (TextView) mycontext.findViewById(
                R.id.cache_size_tv);
        cache_container = (RelativeLayout) mycontext.findViewById(
                R.id.container_clean_cash);
        cache_container.setOnClickListener(this);
        dataclean = new DataCleanManager();
        update_container = (RelativeLayout) mycontext.findViewById(
                R.id.container_version_update);
        update_container.setOnClickListener(this);
        container_logout = (RelativeLayout) mycontext.findViewById(R.id.container_logout);
        container_logout.setOnClickListener(this);
        container_about = (RelativeLayout) mycontext.findViewById(R.id.container_about);
        container_about.setOnClickListener(this);
        container_suggestion = (RelativeLayout) mycontext.findViewById(R.id.container_suggestion);
        container_suggestion.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_setting, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_clean_cash: // 清除缓存
//                dataclean.cleanCustomCache("/data/data/com.example.administrator.bazipaipan/files");
                BmobPro.getInstance(mycontext).clearCache();
                mycontext.toast("缓存清除成功");
                get_cache_size();
                break;
            case R.id.container_version_update: // 版本更新
                mycontext.startActivity(new Intent(mycontext,
                        UpdateVersionActivity.class));
                break;

            case R.id.container_logout: // 退出登录 ①提交app使用信息到服务器端 ②清除账户缓存信息  注销

                BmobUtils.onCancelPressed(mycontext);
                //mainactivity没有finisah
                break;
//意见反馈
            case R.id.container_suggestion:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MeContainerActivity.class);
                intent.putExtra(BaseActivity.PAGETO, SuggestionFragment.TAG);
                mycontext.startActivity(intent);
                break;
            //关于
            case R.id.container_about:

                break;

            default:
                break;
        }
    }

    //----------------1.8    -------------------
    @Override
    public void onToggleStateChange(boolean b) {

    }
}
