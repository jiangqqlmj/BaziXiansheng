package com.example.administrator.bazipaipan.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.me.view.fragment.EditInfoFragment;
import com.example.administrator.bazipaipan.me.view.fragment.MeFragment;
import com.example.administrator.bazipaipan.me.view.fragment.MyFocusFragment;
import com.example.administrator.bazipaipan.me.view.fragment.PayFragment;
import com.example.administrator.bazipaipan.me.view.fragment.RechargeFragment;
import com.example.administrator.bazipaipan.me.view.fragment.SettingFragment;
import com.example.administrator.bazipaipan.me.view.fragment.SuggestionFragment;

import butterknife.ButterKnife;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MeContainerActivity extends MyActivity {
    public static final String TAG = "MeContainerActivity";
    //fragment初始化及实例化
    MeFragment meFragment;
    ActionBar ab;
    //actionbar错位问题
    int actionBarHeight;
    FrameLayout fl_frag_container_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_me);
        ButterKnife.inject(this);
        initViews();
        meFragment = new MeFragment();
        //switchFrag
        mPageTo = MeContainerActivity.this.getIntent().getStringExtra(PAGETO);
        if (mPageTo == null) {
            mPageTo = "MeFragment";
        }
        switchFragment(mPageTo);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(true);
            ab.setTitle("个人中心");
        }
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
            case EditInfoFragment.TAG:
                fragment = EditInfoFragment.newInstance();
                ab.setTitle("编辑资料");
                break;
            case MyFocusFragment.TAG:
                fragment = MyFocusFragment.newInstance();
                ab.setTitle("我的关注");
                break;
            case RechargeFragment.TAG:
                fragment = RechargeFragment.newInstance();
                ab.setTitle("充值");
                break;
            case SettingFragment.TAG:
                fragment = SettingFragment.newInstance();
                ab.setTitle("设置");
                break;
            case SuggestionFragment.TAG:
                fragment = SuggestionFragment.newInstance();
                ab.setTitle("意见反馈");
                break;
            case MeFragment.TAG:
                fragment = MeFragment.newInstance();
                ab.setTitle("个人中心");
                break;
            case PayFragment.TAG:
                fragment = PayFragment.newInstance();
                ab.setTitle("支付");
                break;
            default:
                fragment = MeFragment.newInstance();
                ab.setTitle("个人中心");
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag_container_me, fragment).commit();
        }

    }

}
