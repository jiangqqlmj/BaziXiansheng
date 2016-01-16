package com.example.administrator.bazipaipan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MyPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;
    private List<Fragment> datas;
    FragmentManager fragmentManager;

    public MyPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerFragmentAdapter(FragmentManager fm, List<Fragment> datas, MainActivity activity) {
        super(fm);
        this.activity = activity;
        this.datas = datas;
    }

    public List<Fragment> getDatas() {
        return datas;
    }

    public void setDatas(List<Fragment> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

}
