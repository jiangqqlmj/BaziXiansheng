package com.example.administrator.bazipaipan.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.adapter.MyChatMsgAdapter;
import com.example.administrator.bazipaipan.chat.fragment.ChatContributeFragment;
import com.example.administrator.bazipaipan.chat.fragment.ChatHistoryFragment;
import com.example.administrator.bazipaipan.chat.fragment.ChatLookerFragment;
import com.example.administrator.bazipaipan.chat.fragment.ChatQueueFragment;
import com.example.administrator.bazipaipan.chat.model.MyChatMsg;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 王中阳 on 2015/12/25.
 */
public class ChatContainerActivity extends MyActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, TextView.OnEditorActionListener {
    public static final String TAG = "ChatContainerActivity";
    private ChatContainerActivity mycontext;
    //自定义测试
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private ChatLookerFragment chatLookerFragment;
    private ChatQueueFragment chatQueueFragment;
    private ChatContributeFragment chatContributeFragment;
    //actionbar相关
    ActionBar ab;
    //聊天列表
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //控件
    private LinearLayout chatmain_container_focusnum_sel, chatmain_container_focusnum;
    private TextView tv_chatmain_focusnum, tv_chatmain_focusnum_sel;
    //测算历史
    private ChatHistoryFragment chatHistoryFragment;
    private FrameLayout fl_container_chatmain_history;
    private MenuItem item;
    //下部围观区整体
    private LinearLayout ll_container_allbottom;
    public Intent mIntent;
    //单聊逻辑
    //2view对象
    private ListView recyclerView;
    //3数据源
    private List<MyChatMsg> datas;
    //4适配器
    private MyChatMsgAdapter mAdapter;
    //控件
    private EditText et_chatlooker_input;
    private EMConversation conversation;
    //发送给谁  大师或者是命主  非游客身份
    String tochatusername = "1452678871898";
    NewMessageBroadcastReceiver msgReceiver;
    boolean isfirst;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        mycontext = this;
        initviews();
        //群聊的groupid
//        tochatusername = this.getIntent().getStringExtra(AugurFragment.AUGURID);
        conversation = EMChatManager.getInstance().getConversation("1452678871898");
        updateUI();
//        if (BmobUtils.getCurrentUser(mycontext).getType().equals("2")) {//大师  发给顾客
//            tochatusername = conversation.getUserName();  //谁发给我，我发给谁
//        } else {//非大师 发给大师
//            tochatusername = this.getIntent().getStringExtra(AugurFragment.AUGURID);
//        }
        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
        EMChat.getInstance().setAppInited();
    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();

            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            }
            //更新消息
            conversation.addMessage(message);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            recyclerView.setSelection(recyclerView.getCount() - 1);
        }
    }

    //显示聊天列表
    private void updateUI() {
        recyclerView = (ListView) this.findViewById(R.id.rv_chat_list);
        mAdapter = new MyChatMsgAdapter(conversation, this);
        recyclerView.setAdapter(mAdapter);
        updateFromNet();
    }

    private void updateFromNet() {
        //查询多条数据
    }


    //----------以下是actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        chatHistoryFragment = new ChatHistoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_bagua:
                break;
            case R.id.action_history:
                fm.beginTransaction().add(R.id.fl_container_chatmain_history, chatHistoryFragment).commit();
                break;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.up1024.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //---------以上是actionbar

    private void initviews() {
        //单聊
        et_chatlooker_input = (EditText) findViewById(R.id.et_chatlooker_input);
        et_chatlooker_input.setOnEditorActionListener(this);
        //测算历史
        fl_container_chatmain_history = (FrameLayout) findViewById(R.id.fl_container_chatmain_history);
        //围观区整体
        ll_container_allbottom = (LinearLayout) findViewById(R.id.ll_container_allbottom);
        ll_container_allbottom.setOnClickListener(this);
        //关注切换
        chatmain_container_focusnum = (LinearLayout) findViewById(R.id.chatmain_container_focusnum);
        chatmain_container_focusnum.setOnClickListener(this);
        chatmain_container_focusnum_sel = (LinearLayout) findViewById(R.id.chatmain_container_focusnum_sel);
        chatmain_container_focusnum_sel.setOnClickListener(this);
        tv_chatmain_focusnum = (TextView) findViewById(R.id.tv_chatmain_focusnum);
        tv_chatmain_focusnum.setOnClickListener(this);
        tv_chatmain_focusnum_sel = (TextView) findViewById(R.id.tv_chatmain_focusnum_sel);
        tv_chatmain_focusnum_sel.setOnClickListener(this);

        //        actionbar的切换
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
//            给左上角图标的左边加上一个返回的图标
            ab.setDisplayHomeAsUpEnabled(true);
            //隐藏icon logo
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
        }

        //tabview及viewpager联动效果
        mViewPager = (ViewPager) mycontext.findViewById(R.id.chat_vp_view);
        mTabLayout = (TabLayout) mycontext.findViewById(R.id.chat_tabs);
        //添加页卡视图
        mFragmentList.add(chatLookerFragment);
        mFragmentList.add(chatQueueFragment);
        mFragmentList.add(chatContributeFragment);
        //添加页卡标题
        mTitleList.add("围观区");
        mTitleList.add("排队");
        mTitleList.add("贡献");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        MyChatPagerAdapter mAdapter = new MyChatPagerAdapter(getSupportFragmentManager(), mFragmentList, mycontext);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setOnTabSelectedListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTab(mViewPager.getCurrentItem());
    }

    //切换 抽取
    public void changeTab(int i) {
        switch (i) {
            case 0:
                chatLookerFragment.onRefresh();
                break;
            case 1:
                chatQueueFragment.onRefresh();
                break;
            case 2:
                chatContributeFragment.onRefresh();
                break;
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(mycontext.getSupportFragmentManager());
        chatLookerFragment = new ChatLookerFragment();
        fragmentAdapter.addFragment(chatLookerFragment, getString(R.string.chatlooker));
        chatQueueFragment = new ChatQueueFragment();
        fragmentAdapter.addFragment(chatQueueFragment, getString(R.string.chatqueue));
        chatContributeFragment = new ChatContributeFragment();
        fragmentAdapter.addFragment(chatContributeFragment, getString(R.string.chatcontribute));
        viewPager.setAdapter(fragmentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatmain_container_focusnum:
                chatmain_container_focusnum_sel.setVisibility(View.VISIBLE);
                chatmain_container_focusnum.setVisibility(View.GONE);
                tv_chatmain_focusnum_sel.setText("100");
                break;
            case R.id.chatmain_container_focusnum_sel:
                chatmain_container_focusnum_sel.setVisibility(View.GONE);
                chatmain_container_focusnum.setVisibility(View.VISIBLE);
                tv_chatmain_focusnum.setText("99");
                break;
            case R.id.chat_tabs:
                fl_container_chatmain_history.setVisibility(View.GONE);
                break;
            case R.id.chat_vp_view:
                fl_container_chatmain_history.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //监听键盘发送功能
        String sendmsg;
        sendmsg = et_chatlooker_input.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (TextUtils.isEmpty(sendmsg)) {
                return false;
            } else {
                //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                EMConversation conversation = EMChatManager.getInstance().getConversation(tochatusername);
                //创建一条文本消息
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                //如果是群聊，设置chattype,默认是单聊
                message.setChatType(EMMessage.ChatType.GroupChat);
                //设置消息body
                TextMessageBody txtBody = new TextMessageBody(sendmsg);
                message.addBody(txtBody);
                //设置接收人
                message.setReceipt(tochatusername);
                //把消息加入到此会话对象中
                conversation.addMessage(message);
                //更新聊天列表消息
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                recyclerView.setSelection(recyclerView.getCount() - 1);

                //发送消息
                EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                //发送完毕清空回话框
                et_chatlooker_input.setText("");
            }
            return true;
        }
        return false;
    }

    //切换
    static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //tabview点击效果
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        mViewPager.setCurrentItem(pos);
        changeTab(pos);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //适配器
    class MyChatPagerAdapter extends FragmentPagerAdapter

    {
        private ChatContainerActivity activity;
        private List<Fragment> datas;

        public MyChatPagerAdapter(FragmentManager fm, List<Fragment> datas, ChatContainerActivity activity) {
            super(fm);
            this.activity = activity;
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            Log.e("datas", datas.size() + "");
            return datas.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

        ////添加页卡 初始化item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //移除之前的viewpager数据 待缓存优化
        mFragmentList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册广播
        unregisterReceiver(msgReceiver);
    }
}
