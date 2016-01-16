package com.example.administrator.bazipaipan.amuse.view.activity.fragment;

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
import android.widget.ImageView;

import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.activity.AmuseContainerActivity;
import com.example.administrator.bazipaipan.amuse.view.activity.adapter.AmuseDetailAdapter;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseDetail;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseDetailFragment extends Fragment implements AmuseDetailAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "AmuseDetailFragment";
    //1context
    private AmuseContainerActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<AmuseDetail> datas;
    //4适配器
    private AmuseDetailAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    private ImageView iv_back;

    public static AmuseDetailFragment newInstance() {
        AmuseDetailFragment fragment = new AmuseDetailFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (AmuseContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        //接收上一级传递的intent数据还没有写

    }

    private void initviews() {
        iv_back = (ImageView) mycontext.findViewById(R.id.amuse_iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycontext.finish();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_amuse_detail, container, false);
        updateUI(view);
        return view;
    }

    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_amuse_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //适配器未装填内容
        mAdapter = new AmuseDetailAdapter(mycontext, this);
        datas = new ArrayList<>();
        //假数据
//        for (int i = 0; i < 30; i++) {
//            datas.add(new AmuseDetail("" + i, "" + i, "" + i, "" + i));
//        }
        mAdapter.setMdatas(datas);
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

    @Override
    public void onRefresh() {
        updateFromNet();
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //bmob查询多条数据
        BmobQuery<AmuseDetail> query = new BmobQuery<AmuseDetail>();
        query.setLimit(10);
        //执行查询方法
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(mycontext, new FindListener<AmuseDetail>() {
            @Override
            public void onSuccess(List<AmuseDetail> object) {
                // TODO Auto-generated method stub
                for (AmuseDetail bean : object) {
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
                mycontext.toast("查询失败：" + msg);
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }
        });
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

    //点击item的跳转逻辑  根据position取到objectid
    @Override
    public void onItemClicked(int position) {
        mycontext.toast("amuseFragment 点击了item" + position + "传递的分类id是: " + position);
        List<AmuseDetail> list = mAdapter.getMdatas();
        if (list != null && list.size() > 0) {
            //添加一个标记位，在activity中据此做switch case的区分(多个数据引入bundle)
            Intent intent = new Intent(mycontext, AmuseContainerActivity.class);
            AmuseDetail bean = list.get(position);
            //还需要传递关联关系
            intent.putExtra(AmuseFragment.EXTRAL_DATA, bean);
            intent.putExtra(MyActivity.PAGETO, AmuseCommentFragment.TAG);
            mycontext.startActivity(intent);
        }
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

}
