package com.example.administrator.bazipaipan.homepage.view.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.homepage.view.fragment.adapter.RecommendLookerAdapter;
import com.example.administrator.bazipaipan.widget.FullyGridLayoutManager;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class RecommendLookerFragment extends Fragment implements RecommendLookerAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "RecommendLookerFragment";
    //1context
    private MainActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<Augur> datas;
    //4适配器
    private RecommendLookerAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MainActivity) context;
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
        View view = inflater.inflate(R.layout.fragment_recommend_looker, container, false);
        updateUI(view);
        return view;
    }

    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recommend_looker);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new FullyGridLayoutManager(recyclerView.getContext(), 3));
        //适配器未装填内容
        mAdapter = new RecommendLookerAdapter(mycontext, this);
        datas = new ArrayList<>();
        recyclerView.setAdapter(mAdapter);
        //滑动加载更多的layout
        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright);
        //需要实现方法     测试时暂时关闭
        mSwipeLayout.setOnRefreshListener(this);
        //刷新数据
        onRefresh();
    }

    @Override
    public void onRefresh() {
        updateFromNet();
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //查询多条数据
        BmobQuery<Augur> query = new BmobQuery<Augur>();
        datas = new ArrayList<>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, Augur.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        query.findObjects(mycontext, new FindListener<Augur>() {
            @Override
            public void onSuccess(List<Augur> object) {
                // TODO Auto-generated method stub
                datas.clear();
                for (Augur augurbean : object) {
                    datas.add(augurbean);
                }
                mAdapter.setMdatas(datas);
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(0);
                if (mSwipeLayout != null)
                    mSwipeLayout.setRefreshing(false);
            }
        });

    }

    //点击item的跳转逻辑
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(mycontext, ChatContainerActivity.class);
        mycontext.startActivity(intent);

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
                    if (mSwipeLayout != null) {
                        if (mSwipeLayout.isRefreshing()) {
                            mSwipeLayout.setRefreshing(false);
                        }
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
    public void onResume() {
        super.onResume();
        onRefresh();
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
