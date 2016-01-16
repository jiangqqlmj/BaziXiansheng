package com.example.administrator.bazipaipan.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.login.fragment.LoginFragment;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by 王中阳 on 2015/12/16.`
 */
public class LoginContainerActivity extends MyActivity {
    public static final String TAG = "LoginContainerActivity";
    public static final String TAG_FRAG = "LoginFragment";
    //fragment初始化及实例化
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_login);
        ButterKnife.inject(this);
        Cache();
        initViews();
        loginFragment = new LoginFragment();
        //switchFrag
        mPageTo = TAG_FRAG;
        switchFragment(mPageTo);
    }

    private void Cache() {
        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if (bmobUser != null) {
            // 允许用户使用应用
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            mPageTo = TAG_FRAG;
            switchFragment(mPageTo);
        }
    }

    private void initViews() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        if (ab != null) {
////            ab.setHomeAsUpIndicator(R.drawable.back);
////            ab.setDisplayHomeAsUpEnabled(true);
////            ab.setDisplayShowTitleEnabled(true);
////            ab.setTitle("登录");
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //onclick事件+switch case  跳转切换Fragment
    public void switchFragment(String pageTo) {
        Fragment fragment;
        switch (pageTo) {
            default:
                fragment = LoginFragment.newInstance();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag_container_login, fragment).commit();
            //销毁登录act   bug注销之后无法再次登录  所以把登录注册逻辑分开
        }

    }


}
