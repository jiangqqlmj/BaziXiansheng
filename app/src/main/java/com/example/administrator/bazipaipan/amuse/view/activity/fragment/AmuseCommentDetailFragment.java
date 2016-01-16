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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.activity.AmuseContainerActivity;
import com.example.administrator.bazipaipan.amuse.view.activity.adapter.AmuseAdapter;
import com.example.administrator.bazipaipan.amuse.view.activity.adapter.AmuseCommentAdapter;
import com.example.administrator.bazipaipan.amuse.view.activity.model.ChatComment;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseCommentDetailFragment extends Fragment implements AmuseAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener, View.OnClickListener {
    public static final String TAG = "AmuseCommentDetailFragment";
    //1context
    private AmuseContainerActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<ChatComment> datas;
    //4适配器
    private AmuseCommentAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6fragment的跳转逻辑
    FragmentManager fm;
    FragmentTransaction ft;
    //7数据传递
    public static final String EXTRAL_DATA = "extral_data";
    private EditText et_my_comment;

    //点击返回
    private ImageView back, iv_share;


    //把这个抽取到myfragment
    public static AmuseCommentDetailFragment newInstance() {
        AmuseCommentDetailFragment fragment = new AmuseCommentDetailFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (AmuseContainerActivity) context;
        fm = mycontext.getFragmentManager();
//        ft = fm.beginTransaction();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
    }

    private void initviews() {
        //发表评论
        et_my_comment = (EditText) mycontext.findViewById(R.id.et_my_comment);
        et_my_comment.setOnEditorActionListener(this);
        //分享
        iv_share = (ImageView) mycontext.findViewById(R.id.iv_share_btn);
        iv_share.setOnClickListener(this);
        back = (ImageView) mycontext.findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycontext.finish();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_amuse_four, container, false);
        updateUI(view);
//        back = (ImageView) mycontext.findViewById(R.id.iv_back);
        return view;
    }

    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fv_amuse);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
//        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        //适配器未装填内容
        mAdapter = new AmuseCommentAdapter(mycontext);
        //模拟假数据

        datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add(new ChatComment("" + i, "" + i, false, null));
        }
        //bmob模拟数据
//        for (int i = 0; i < datas.size(); i++) {
//            Log.e("datas", datas.get(i).getAmuseLookerNum() + "," + datas.get(i).getAmuseTitle());
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

    //耗时操作抓取数据需要用handler机制
//    private List<AmuseCategory> bmobNetDatas() {
//
//    }

    @Override
    public void onRefresh() {
        updateFromNet();
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //bmob查询多条数据
        BmobQuery<ChatComment> query = new BmobQuery<ChatComment>();
        query.setLimit(10);
        //执行查询方法
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(mycontext, new FindListener<ChatComment>() {
            @Override
            public void onSuccess(List<ChatComment> object) {
                // TODO Auto-generated method stub
                for (ChatComment bean : object) {
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
//        mycontext.toast("amuseFragment 点击了item" + position + "传递的分类id是: " + position);
//        List<ChatComment> list = mAdapter.getMdatas();
//        if (list != null && list.size() > 0) {
//            //添加一个标记位，在activity中据此做switch case的区分(多个数据引入bundle)
//            Intent intent = new Intent(mycontext, AmuseContainerActivity.class);
//            ChatComment bean = list.get(position);
//            intent.putExtra(AmuseCommentDetailFragment.EXTRAL_DATA, bean);
//            intent.putExtra(MyActivity.PAGETO, AmuseDetailFragment.TAG);
//            mycontext.startActivity(intent);
//        }
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //监听键盘发送功能
        String sendmsg;
        sendmsg = et_my_comment.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (TextUtils.isEmpty(sendmsg)) {
                mycontext.toast("请输入内容");
                return false;
            } else {
                mycontext.toast("发送：" + sendmsg);
                et_my_comment.setText("");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share_btn:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.up1024.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.amuse_back:
                mycontext.finish();
        }
    }
}