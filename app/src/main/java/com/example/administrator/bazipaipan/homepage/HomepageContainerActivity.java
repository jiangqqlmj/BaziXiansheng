package com.example.administrator.bazipaipan.homepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.homepage.view.fragment.fragment.MingpanFragment;
import com.example.administrator.bazipaipan.me.view.fragment.EditInfoFragment;

import butterknife.ButterKnife;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class HomepageContainerActivity extends BaseActivity {
    public static final String TAG = "HomepageContainerActivity";
    //fragment初始化及实例化
    MingpanFragment mingpanFragment;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_homepage);
        ButterKnife.inject(this);
        initViews();
        mingpanFragment = new MingpanFragment();
        //switchFrag
        mPageTo = HomepageContainerActivity.this.getIntent().getStringExtra(PAGETO);
        if (mPageTo == null) {
            mPageTo = "mingpanFragment";
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
            ab.setTitle("我的命盘");
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
                fragment = MingpanFragment.newInstance();
                ab.setTitle("我的命盘");
                break;
            default:
                fragment = MingpanFragment.newInstance();
                ab.setTitle("我的命盘");
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag_container_homepage, fragment).commit();
        }

    }

}
