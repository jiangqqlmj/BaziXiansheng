package com.example.administrator.bazipaipan.augur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseCommentDetailFragment;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseCommentFragment;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseDetailFragment;

import butterknife.ButterKnife;

/**
 * Created by 王中阳 on 2015/12/15.
 * 思路：
 * 1首页4个模块 以mainactivity为容器
 * 2 4个模块各自创建 activitycontainer作为容器
 */
public class AugurContainerActivity extends BaseActivity {
    public static final String TAG = "AmuseContainerActivity";
    AmuseDetailFragment amuseDetailFragment;
    //标题栏
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_amuse);
        ButterKnife.inject(this);
        initViews();
        amuseDetailFragment = new AmuseDetailFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_frag_container_amuse, amuseDetailFragment).commit();
        //switch case逻辑区别
        if (AugurContainerActivity.this.getIntent().getStringExtra(PAGETO) == null) {
            return;
        }
        mPageTo = AugurContainerActivity.this.getIntent().getStringExtra(PAGETO);
        if (mPageTo == null) {
            mPageTo = "AmuseDetailFragment";
        }
        switchFragment(mPageTo);
    }

    private void switchFragment(String mPageTo) {
        Fragment fragment;
        switch (mPageTo) {
            case AmuseDetailFragment.TAG:
                fragment = AmuseDetailFragment.newInstance();
                break;
            case AmuseCommentFragment.TAG:
                fragment = AmuseCommentFragment.newInstance();
                break;
            case AmuseCommentDetailFragment.TAG:
                fragment = AmuseCommentDetailFragment.newInstance();
                ab.setTitle("评论");
                break;
            default:
                fragment = AmuseDetailFragment.newInstance();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_frag_container_amuse, fragment).commit();
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
            //换back键图标
//            ab.setHomeAsUpIndicator(R.drawable.back);
//            给左上角图标的左边加上一个返回的图标
            ab.setDisplayHomeAsUpEnabled(true);
            //隐藏icon logo
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
            //fragment+titlebar的问题
            ab.setTitle("人在江湖");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.about_licence:
//                Intent intent = new Intent(v.getContext(), LicenceActivity.class);
//                startActivity(intent);
//            break;
//        }
//    }
}