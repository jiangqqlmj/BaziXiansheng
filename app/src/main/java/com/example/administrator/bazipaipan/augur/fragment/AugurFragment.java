package com.example.administrator.bazipaipan.augur.fragment;

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

import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.augur.adapter.AugurAdapter;
import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.chat.huanxin.activity.ChatActivity;
import com.example.administrator.bazipaipan.chat.huanxin.applib.controller.HXSDKHelper;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AugurFragment extends Fragment implements AugurAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "AugurFragment";
    public static final String AUGURID = "augurid";  //环信的username
    //1context
    private MainActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<Augur> datas;
    private List<EMGroupInfo> chat_datas;
    //4适配器
    private AugurAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6数据传递  item跳转
    public static final String EXTRAL_DATA = "extral_data";
    //--chat
    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    HXContactInfoSyncListener contactInfoSyncListener;

    class HXContactInfoSyncListener implements HXSDKHelper.HXSyncListener {

        @Override
        public void onSyncSucess(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    progressBar.setVisibility(View.GONE);
                    if (success) {
                        refresh();
                    }
                }
            });
        }

    }

    // 刷新ui
    public void refresh() {
        try {
            // 可能会在子线程中调到这方法
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
//                    getContactList();
//                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--chat
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        getBombId();
    }


    private void getBombId() {
    }

    private void initviews() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_augur, container, false);
        updateUI(view);
        return view;
    }

    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_augur);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new AugurAdapter(mycontext, this);
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

    //刷新加载更多数据
    @Override
    public void onRefresh() {
        updateFromNet();    //bmob
        getChatList();      //环信聊天
    }

    //获取群聊列表
    private void getChatList() {
        EMGroupManager.getInstance().loadAllGroups();
        EMChatManager.getInstance().loadAllConversations();
        chat_datas = new ArrayList<>();
        try {
            EMGroupManager.getInstance().asyncGetAllPublicGroupsFromServer(new EMValueCallBack<List<EMGroupInfo>>() {
                @Override
                public void onSuccess(List<EMGroupInfo> value) {
                    for (EMGroupInfo info : value) {
                        //给augur添加字段，及群组名称
                        chat_datas.add(info);
                    }
                    mAdapter.setChat_mdatas(chat_datas);
                    mHandler.sendEmptyMessage(0);
                    mSwipeLayout.setRefreshing(false);
                }

                @Override
                public void onError(int error, String errorMsg) {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(0);
                    mSwipeLayout.setRefreshing(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //查询多条数据
        BmobQuery<Augur> query = new BmobQuery<Augur>();
        query.setLimit(10);
        datas = new ArrayList<>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, Augur.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.findObjects(mycontext, new FindListener<Augur>() {
            @Override
            public void onSuccess(List<Augur> object) {
                // TODO Auto-generated method stub
                for (Augur augurbean : object) {
                    //数据源填充
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
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    //点击item的跳转逻辑
    @Override
    public void onItemClicked(int position) {
//        List<Augur> list = mAdapter.getMdatas();
        List<EMGroupInfo> list = mAdapter.getChat_mdatas();
        if (list != null && list.size() > 0) {
            //如果没有创建房间则不能跳转
            EMGroupInfo bean = list.get(position);
            Intent intent = new Intent(mycontext, ChatActivity.class);
            //进入到群组
//            try {
//                EMGroupManager.getInstance().joinGroup(bean.getGroupId());//需异步处理
            intent.putExtra("groupId", bean.getGroupId());
            intent.putExtra("chatType", CHATTYPE_GROUP);
//            BmobUtils.log("聊天跳转" + EMGroupManager.getInstance().getGroup(bean.getGroupId()).getOwner());
//            }
//            catch (EaseMobException e) {
//                e.printStackTrace();
//            }
            mycontext.startActivity(intent);

//            if (bean.getAugur_pointer().getIsCreatedGroup() != null && bean.getAugur_pointer().getIsCreatedGroup().equals("2")) {
//            } else {
//                mycontext.toast("该大师尚未创建房间");
//            }
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
