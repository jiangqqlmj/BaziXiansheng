package com.example.administrator.bazipaipan.chat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.chat.adapter.ChatGiftAdapter;
import com.example.administrator.bazipaipan.chat.adapter.ChatLookerAdapter;
import com.example.administrator.bazipaipan.chat.model.Gift;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.fragment.RechargeFragment;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * Created by 王中阳 on 2016/1/2.
 */
public class ChatLookerFragment extends Fragment implements ChatLookerAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener, View.OnClickListener, ChatGiftAdapter.IClickListener {

    public static final String TAG = "ChatLookerFragment";
    //1context
    private ChatContainerActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<MyUser> datas;
    //4适配器
    private ChatLookerAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //控件
    private ImageView iv_chatlooker_sendpic;
    private EditText et_chatlooker_input;
    private ImageView iv_chatlooker_givegift;
    //赠送礼物
    private RecyclerView rv_chatgift;
    private Button btn_chatgift_recharge;
    private TextView tv_giftpop_hot, tv_giftpop_all;
    private LinearLayout chatlooker_container_givegift; //最外层容器
    private ChatGiftAdapter chatGiftAdapter;
    private List<Gift> giftdatas;
    //------2级礼物
    private LinearLayout givegift_container_second, givegift_container_first;
    private TextView tv_goldnum_givegift, tv_rechargebtn, tv_givegift_second_name, tv_givegift_second_goldnum;
    private ImageView iv_givegift_second_head, iv_back_givegift;
    private Button btn_givegift_second;
    //聊天
    private ImageView iv_chatlooker_voice, iv_chatlooker_key;
    private LinearLayout container_chatlooker_talkaugur, container_chatlooker_talkall;

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (ChatContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlooker, container, false);
        et_chatlooker_input = (EditText) view.findViewById(R.id.et_chatlooker_input);
        et_chatlooker_input.setOnEditorActionListener(this);
        iv_chatlooker_givegift = (ImageView) view.findViewById(R.id.iv_chatlooker_givegift);
        iv_chatlooker_givegift.setOnClickListener(this);
        initTalk(view);
        initGift(view);
        updateUI(view);
        return view;
    }

    //聊天
    private void initTalk(View view) {
        iv_chatlooker_voice = (ImageView) view.findViewById(R.id.iv_chatlooker_voice);
        iv_chatlooker_voice.setOnClickListener(this);
        iv_chatlooker_key = (ImageView) view.findViewById(R.id.iv_chatlooker_key);
        iv_chatlooker_key.setOnClickListener(this);
        container_chatlooker_talkaugur = (LinearLayout) view.findViewById(R.id.container_chatlooker_talkaugur);
        container_chatlooker_talkall = (LinearLayout) view.findViewById(R.id.container_chatlooker_talkall);
    }

    private void initGift(View view) {
        //2级礼物
        iv_givegift_second_head = (ImageView) view.findViewById(R.id.iv_givegift_second_head);
        iv_back_givegift = (ImageView) view.findViewById(R.id.iv_back_givegift);
        iv_back_givegift.setOnClickListener(this);
        givegift_container_first = (LinearLayout) view.findViewById(R.id.givegift_container_first);
        givegift_container_first.setOnClickListener(this);
        givegift_container_second = (LinearLayout) view.findViewById(R.id.givegift_container_second);
        givegift_container_second.setOnClickListener(this);
        tv_goldnum_givegift = (TextView) view.findViewById(R.id.tv_goldnum_givegift);
        tv_rechargebtn = (TextView) view.findViewById(R.id.tv_rechargebtn);
        tv_rechargebtn.setOnClickListener(this);
        tv_givegift_second_name = (TextView) view.findViewById(R.id.tv_givegift_second_name);
        tv_givegift_second_goldnum = (TextView) view.findViewById(R.id.tv_givegift_second_goldnum);
        btn_givegift_second = (Button) view.findViewById(R.id.btn_givegift_second);
        btn_givegift_second.setOnClickListener(this);
        //1级礼物
        btn_chatgift_recharge = (Button) view.findViewById(R.id.btn_chatgift_recharge);
        btn_chatgift_recharge.setOnClickListener(this);
        tv_giftpop_hot = (TextView) view.findViewById(R.id.tv_giftpop_hot);
        tv_giftpop_hot.setOnClickListener(this);
        tv_giftpop_all = (TextView) view.findViewById(R.id.tv_giftpop_all);
        tv_giftpop_all.setOnClickListener(this);
        chatlooker_container_givegift = (LinearLayout) view.findViewById(R.id.chatlooker_container_givegift);
        chatlooker_container_givegift.setOnClickListener(this);

        rv_chatgift = (RecyclerView) view.findViewById(R.id.rv_chatgift);
        //方向
        rv_chatgift.setLayoutManager(new LinearLayoutManager(rv_chatgift.getContext(), HORIZONTAL, false));
        //子布局装饰
        rv_chatgift.addItemDecoration(new DividerItemDecoration(getContext(), HORIZONTAL));
        //适配器未装填内容
        chatGiftAdapter = new ChatGiftAdapter(mycontext, this);
        rv_chatgift.setAdapter(chatGiftAdapter);
        //滑动加载更多的layout
//        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_bright);
//        //需要实现方法
//        mSwipeLayout.setOnRefreshListener(this);
        updateGiftDatas();
    }

    //更新赠送礼物的数据
    private void updateGiftDatas() {
        //查询多条数据
        BmobQuery<Gift> query = new BmobQuery<>();
        query.setLimit(10);
        giftdatas = new ArrayList<>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, Gift.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.findObjects(mycontext, new FindListener<Gift>() {
            @Override
            public void onSuccess(List<Gift> object) {
                // TODO Auto-generated method stub
                for (Gift bean : object) {
                    //数据源填充
                    giftdatas.add(bean);
                }
                chatGiftAdapter.setMdatas(giftdatas);
//                mHandler.sendEmptyMessage(0);
//                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
//                mHandler.sendEmptyMessage(0);
//                mSwipeLayout.setRefreshing(false);
            }
        });

    }


    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_chatlooker);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //适配器未装填内容
        mAdapter = new ChatLookerAdapter(mycontext, this);
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
        //查询多条数据
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.setLimit(10);
        datas = new ArrayList<>();
        query.findObjects(mycontext, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> object) {
                // TODO Auto-generated method stub
                for (MyUser bean : object) {
                    //数据源填充
                    datas.add(bean);
                }
                mAdapter.setMdatas(datas);

                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
//                mycontext.toast("查询失败：" + msg);
                mHandler.sendEmptyMessage(0);
                if (mSwipeLayout == null) {
                    return;
                }
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    //点击item的跳转逻辑  赠送礼物跳转
    @Override
    public void onItemClicked(int position) {
        // 逻辑 隐藏 及传参
        secondShow();
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //监听键盘发送功能
        String sendmsg;
        sendmsg = et_chatlooker_input.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (TextUtils.isEmpty(sendmsg)) {
                mycontext.toast("请输入内容");
                return false;
            } else {
                mycontext.toast("发送：" + sendmsg);
                et_chatlooker_input.setText("");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chatlooker_givegift:
                //显示礼物pop
                chatlooker_container_givegift.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_rechargebtn:
                comRecharge();
                break;
            case R.id.btn_chatgift_recharge:
                //充值按钮
                comRecharge();
                break;
            case R.id.tv_giftpop_hot:
                tv_giftpop_hot.setTextColor(getResources().getColor(R.color.tv_chatgold));
                tv_giftpop_all.setTextColor(getResources().getColor(R.color.tv_black));
                break;
            case R.id.tv_giftpop_all:
                tv_giftpop_hot.setTextColor(getResources().getColor(R.color.tv_black));
                tv_giftpop_all.setTextColor(getResources().getColor(R.color.tv_chatgold));
                break;
            case R.id.iv_back_givegift:
                firstShow();
                break;
            case R.id.btn_givegift_second:
//                mycontext.toast("礼物赠送成功");
                //金币不足， dialog
                rechargeDialog();
                break;
            //发语音
            case R.id.iv_chatlooker_voice:

                break;
            //键盘
            case R.id.iv_chatlooker_key:

                break;
        }
    }


    //---------抽取方法----------
    private void comRecharge() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MeContainerActivity.class);
        intent.putExtra(BaseActivity.PAGETO, RechargeFragment.TAG);
        mycontext.startActivity(intent);
    }

    private void firstShow() {
        givegift_container_first.setVisibility(View.VISIBLE);
        givegift_container_second.setVisibility(View.GONE);
    }

    private void secondShow() {
        givegift_container_second.setVisibility(View.VISIBLE);
        givegift_container_first.setVisibility(View.GONE);
    }

    protected void rechargeDialog() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mycontext);
        builder.setMessage("金币余额不足，赠送礼物失败");
//        builder.setTitle("提示");
        builder.setNegativeButton("重新选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firstShow();
                dialog.dismiss();
            }

        });
        builder.setPositiveButton("获取金币", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                comRecharge();
                dialog.dismiss();
            }
        });
        builder.create().show();
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