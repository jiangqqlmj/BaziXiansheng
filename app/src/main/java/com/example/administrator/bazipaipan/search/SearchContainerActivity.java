package com.example.administrator.bazipaipan.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 * 搜索user表 username 先写适配器 type 2  userback
 */
public class SearchContainerActivity extends BaseActivity implements MyUserAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {
    public static final String TAG = "SearchContainerActivity";
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<MyUser> datas;
    private List<EMGroupInfo> chat_datas;
    //4适配器
    private MyUserAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6数据传递  item跳转
    public static final String EXTRAL_DATA = "extral_data";
    EditText search_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_search);
        ButterKnife.inject(this);
        initViews();
        updateUI();
    }

    private void initViews() {
        search_et = (EditText) this.findViewById(R.id.search_et);
        search_et.setOnEditorActionListener(this);
    }

    @OnClick(R.id.cancel_search)
    public void cancel() {
        this.finish();
    }

    private void updateUI() {
        recyclerView = (RecyclerView) this.findViewById(R.id.rv_search_augur);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new MyUserAdapter(this, this);
        mAdapter.setMdatas(datas);
        recyclerView.setAdapter(mAdapter);
        //滑动加载更多的layout
        mSwipeLayout = (VerticalSwipeRefreshLayout) this.findViewById(R.id.swipe_container);
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
//        updateFromNet();    //bmob
//        getChatList();      //环信聊天
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
//                    mAdapter.setChat_mdatas(chat_datas);
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
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        datas = new ArrayList<>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        query.findObjects(this, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> object) {
                // TODO Auto-generated method stub
                for (MyUser augurbean : object) {
                    //数据源填充
                    datas.add(augurbean);
                }
//                mAdapter.setMdatas(datas);
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
//        if (list != null && list.size() > 0) {
//            //如果没有创建房间则不能跳转
//            Augur bean = list.get(position);
//            if (bean.getAugur_pointer().getIsCreatedGroup() != null && bean.getAugur_pointer().getIsCreatedGroup().equals("2")) {
//                //添加一个标记位，在activity中据此做switch case的区分(多个数据引入bundle)
//
//            } else {
//                this.toast("该大师尚未创建房间");
//            }
//        }
        Intent intent = new Intent(this, ChatContainerActivity.class);
        this.startActivity(intent);
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

    //user augur去重
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //监听键盘发送功能
        String sendmsg;
        sendmsg = search_et.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (TextUtils.isEmpty(sendmsg)) {
                this.toast("请输入内容");
                return false;
            } else {
                this.toast("搜索" + sendmsg);
                //搜索条件+查询 搜索user之后关联大师
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                //关联查询
                query.addWhereContains("username", sendmsg);
                query.findObjects(this, new FindListener<MyUser>() {
                    @Override
                    public void onSuccess(List<MyUser> list) {
                        datas.addAll(list);
                        mAdapter.setMdatas(datas);
                        mHandler.sendEmptyMessage(0);
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(int i, String s) {
                        SearchContainerActivity.this.toast("没有找到，换个关键字试试");
                        BmobUtils.log("bmob模糊查询" + s);
                        mHandler.sendEmptyMessage(0);
                        mSwipeLayout.setRefreshing(false);

                    }
                });
            }
            return true;
        }
        return false;
    }
}
