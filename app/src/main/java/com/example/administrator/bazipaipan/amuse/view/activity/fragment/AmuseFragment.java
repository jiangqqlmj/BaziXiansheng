package com.example.administrator.bazipaipan.amuse.view.activity.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.activity.AmuseContainerActivity;
import com.example.administrator.bazipaipan.amuse.view.activity.adapter.AmuseAdapter;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseCategory;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 * <p/>
 * 江湖首页
 */
public class AmuseFragment extends Fragment implements AmuseAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "AmuseFragment";
    //1context
    private MainActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<AmuseCategory> datas;
    //4适配器
    private AmuseAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6fragment的跳转逻辑
    FragmentManager fm;
    FragmentTransaction ft;
    //7数据传递
    public static final String EXTRAL_DATA = "extral_data";

    //把这个抽取到myfragment
    public static AmuseFragment newInstance() {
        AmuseFragment fragment = new AmuseFragment();
        return fragment;
    }

    public AmuseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MainActivity) context;
        fm = mycontext.getFragmentManager();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
    }

    private void initviews() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_amuse_first, container, false);
        updateUI(view);
        return view;
    }

    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_amuse);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        mAdapter = new AmuseAdapter(mycontext, this);
        datas = new ArrayList<>();
        recyclerView.setAdapter(mAdapter);
        //滑动加载更多的layout
        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright);
        //需要实现方法
        mSwipeLayout.setOnRefreshListener(this);
        onRefresh();
    }

    //耗时操作抓取数据需要用handler机制

    @Override
    public void onRefresh() {
        updateFromNet();
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //bmob查询多条数据
        BmobQuery<AmuseCategory> query = new BmobQuery<AmuseCategory>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        //执行查询方法
//        boolean isCache = query.hasCachedResult(mycontext, AmuseCategory.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.findObjects(mycontext, new FindListener<AmuseCategory>() {
            @Override
            public void onSuccess(List<AmuseCategory> object) {
                // TODO Auto-generated method stub
                datas.clear(); //清空原数据之后加载
                for (AmuseCategory bean : object) {
                    datas.add(bean);
                }
                mAdapter.setMdatas(datas);
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
                return;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }
        });

    }

    //点击item的跳转逻辑  根据position取到objectid
    @Override
    public void onItemClicked(int position) {
        List<AmuseCategory> list = mAdapter.getMdatas();
        if (list != null && list.size() > 0) {
            //添加一个标记位，在activity中据此做switch case的区分(多个数据引入bundle)
            Intent intent = new Intent(mycontext, AmuseContainerActivity.class);
            AmuseCategory bean = list.get(position);
//            intent.putExtra(AmuseFragment.EXTRAL_DATA, bean);
            intent.putExtra(BaseActivity.PAGETO, AmuseDetailFragment.TAG);
            mycontext.startActivity(intent);
        }
    }

    //刷新数据相关
    public void setSwipeToRefreshEnabled(boolean enabled) {
        mSwipeLayout.setEnabled(enabled);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mSwipeLayout.isRefreshing()) {
                        mSwipeLayout.setRefreshing(false);
                    }
                    break;
            }
        }
    };

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

}
