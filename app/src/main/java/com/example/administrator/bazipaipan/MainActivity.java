package com.example.administrator.bazipaipan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseFragment;
import com.example.administrator.bazipaipan.augur.fragment.AugurFragment;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.chat.huanxin.Constant;
import com.example.administrator.bazipaipan.chat.huanxin.DemoHXSDKHelper;
import com.example.administrator.bazipaipan.chat.huanxin.activity.ChatActivity;
import com.example.administrator.bazipaipan.chat.huanxin.applib.controller.HXSDKHelper;
import com.example.administrator.bazipaipan.chat.huanxin.db.InviteMessgeDao;
import com.example.administrator.bazipaipan.chat.huanxin.db.UserDao;
import com.example.administrator.bazipaipan.chat.huanxin.domain.InviteMessage;
import com.example.administrator.bazipaipan.chat.huanxin.domain.User;
import com.example.administrator.bazipaipan.homepage.view.fragment.fragment.HomePageFragment;
import com.example.administrator.bazipaipan.login.LoginContainerActivity;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.fragment.MeFragment;
import com.example.administrator.bazipaipan.me.view.fragment.RechargeFragment;
import com.example.administrator.bazipaipan.search.SearchContainerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, EMEventListener {

    private String content[];
    private int img[];
    private LinearLayout layout;
    private ListView mListView;
    private PopupWindow pw;
    //-----------------------------
    private static final String TAG = "MainActivity";
    private MainActivity mycontext;
    private MyPagerFragmentAdapter myadapter;
    private ViewPager vp;
    private List<Fragment> list_frags;
    Fragment home_fragment, augur_fragment, amuse_fragment, me_fragment;
    //未选中和选中图片
    private int[] imgnor = {R.drawable.nor_homepage,
            R.drawable.nor_augur, R.drawable.nor_amuse,
            R.drawable.nor_me};
    private int[] imgsel = {R.drawable.sel_homepage,
            R.drawable.sel_augur,
            R.drawable.sel_amuse,
            R.drawable.sel_me};
    //viewpager中记载的图片
    private int[] imgcont = {R.drawable.show_homepage, R.drawable.show_augur, R.drawable.show_amuse, R.drawable.show_me};
    private LinearLayout[] tabs = new LinearLayout[4];
    //actionbar相关
    ActionBar ab;
    private String[] titles = {"八字排盘", "八字先生", "江湖", "个人中心"};
    private TextView toolbar_title_main;
    //搜索图片切换
    private boolean showmenu;
    //--chat
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private MyConnectionListener connectionListener = null;
    private MyGroupChangeListener groupChangeListener = null;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }


    //--chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //--chat 向下
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHXSDKHelper.getInstance().logout(true, null);
            finish();
            startActivity(new Intent(this, LoginContainerActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginContainerActivity.class));
            return;
        }
        //--chat 向上
        setContentView(R.layout.activity_mymain);
        mycontext = this;
        showmenu = true;
        Bmob.initialize(this, "f93db7bdfd1f0c1657b956588038115f");
        EMChat.getInstance().setAppInited();  //环信 UI初始化完毕  注册监听文档
        initViews();
        //--chat 下
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        //好友请求信息
//        inviteMessgeDao = new InviteMessgeDao(this);
//        userDao = new UserDao(this);
/**
 *  // 这个fragment只显示好友和群组的聊天记录
 // chatHistoryFragment = new ChatHistoryFragment();
 // 显示所有人消息记录的fragment
 chatHistoryFragment = new ChatAllHistoryFragment();
 contactListFragment = new ContactlistFragment();
 settingFragment = new SettingsFragment();
 fragments = new Fragment[]{chatHistoryFragment, contactListFragment, settingFragment};
 // 添加显示第一个fragment
 getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment)
 .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(chatHistoryFragment)
 .commit();
 */
        init();
        //异步获取当前用户的昵称和头像
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncGetCurrentUserInfo();
        //--chat
        ButterKnife.inject(this);
        initDatas();
        myadapter = new MyPagerFragmentAdapter(getSupportFragmentManager(), list_frags, MainActivity.this);
        vp.setAdapter(myadapter);
        vp.setOnPageChangeListener(this);
        //笑傲江湖跳转
        if (MainActivity.this.getIntent().getStringExtra(PAGETO) != null) {
            changeItem(2);
            vp.setCurrentItem(2);
            toolbar_title_main.setText("江湖");
            icon_menu.setImageResource(R.drawable.menu_search);
            showmenu = false;
            list_frags.clear();
            initDatas();
            myadapter.setDatas(list_frags);
        }
    }

    private void init() {
        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener

        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);

        groupChangeListener = new MyGroupChangeListener();
        // 注册群聊相关的listener
        EMGroupManager.getInstance().addGroupChangeListener(groupChangeListener);


        //内部测试方法，请忽略
//        registerInternalDebugReceiver();
    }

    /**
     * MyGroupChangeListener
     */
    public class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            String st3 = getResources().getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + " " + st3));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // 刷新ui
//                    if (currentTabIndex == 0)
//                        chatHistoryFragment.refresh();
//                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
            });

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {

            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
//                    try {
//                        updateUnreadLabel();
//                        if (currentTabIndex == 0)
//                            chatHistoryFragment.refresh();
//                        if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                            GroupsActivity.instance.onResume();
//                        }
//                    } catch (Exception e) {
//                        EMLog.e(TAG, "refresh exception " + e.getMessage());
//                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {

            // 群被解散
            // 提示用户群被解散,demo省略
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
//                    updateUnreadLabel();
//                    if (currentTabIndex == 0)
//                        chatHistoryFragment.refresh();
//                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
            });

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

            // 用户申请加入群聊
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {

            String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + " " + st4));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
//                    // 刷新ui
//                    if (currentTabIndex == 0)
//                        chatHistoryFragment.refresh();
//                    if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
            });
        }

        /**
         * 刷新未读消息数
         */
        public void updateUnreadLabel() {
            int count = getUnreadMsgCountTotal();
            if (count > 0) {
//                unreadLabel.setText(String.valueOf(count));
//                unreadLabel.setVisibility(View.VISIBLE);
            } else {
//                unreadLabel.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            // 刷新ui
//            if (currentTabIndex == 1)
//                contactListFragment.refresh();

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    String st10 = getResources().getString(R.string.have_you_removed);
                    if (ChatActivity.activityInstance != null
                            && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
//                    updateUnreadLabel();
//                    // 刷新ui
//                    contactListFragment.refresh();
//                    chatHistoryFragment.refresh();
                }
            });

        }

        @Override
        public void onContactInvited(String username, String reason) {

            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {

            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    static void asyncFetchGroupsFromServer() {
        HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

            @Override
            public void onSuccess() {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

                if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }
            }

            @Override
            public void onError(int code, String message) {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

        });
    }

    static void asyncFetchContactsFromServer() {
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {

            @Override
            public void onSuccess(List<String> usernames) {
                Context context = HXSDKHelper.getInstance().getAppContext();

                System.out.println("----------------" + usernames.toString());
                EMLog.d("roster", "contacts size: " + usernames.size());
                Map<String, User> userlist = new HashMap<String, User>();
                for (String username : usernames) {
                    User user = new User();
                    user.setUsername(username);
                    setUserHearder(username, user);
                    userlist.put(username, user);
                }
                // 添加user"申请与通知"
                User newFriends = new User();
                newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                String strChat = context.getString(R.string.Application_and_notify);
                newFriends.setNick(strChat);

                userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                // 添加"群聊"
                User groupUser = new User();
                String strGroup = context.getString(R.string.group_chat);
                groupUser.setUsername(Constant.GROUP_USERNAME);
                groupUser.setNick(strGroup);
                groupUser.setHeader("");
                userlist.put(Constant.GROUP_USERNAME, groupUser);

                // 添加"聊天室"
                User chatRoomItem = new User();
                String strChatRoom = context.getString(R.string.chat_room);
                chatRoomItem.setUsername(Constant.CHAT_ROOM);
                chatRoomItem.setNick(strChatRoom);
                chatRoomItem.setHeader("");
                userlist.put(Constant.CHAT_ROOM, chatRoomItem);

                // 添加"Robot"
                User robotUser = new User();
                String strRobot = context.getString(R.string.robot_chat);
                robotUser.setUsername(Constant.CHAT_ROBOT);
                robotUser.setNick(strRobot);
                robotUser.setHeader("");
                userlist.put(Constant.CHAT_ROBOT, robotUser);

                // 存入内存
                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
                // 存入db
                UserDao dao = new UserDao(context);
                List<User> users = new ArrayList<User>(userlist.values());
                dao.saveContactList(users);

                HXSDKHelper.getInstance().notifyContactsSyncListener(true);

                if (HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }

                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<User>>() {

                    @Override
                    public void onSuccess(List<User> uList) {
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).updateContactList(uList);
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }

        });
    }

    static void asyncFetchBlackListFromServer() {
        HXSDKHelper.getInstance().asyncFetchBlackListFromServer(new EMValueCallBack<List<String>>() {

            @Override
            public void onSuccess(List<String> value) {
                EMContactManager.getInstance().saveBlackList(value);
                HXSDKHelper.getInstance().notifyBlackListSyncListener(true);
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyBlackListSyncListener(false);
            }

        });
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    private static void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();

                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);

                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
//                updateUnreadLabel();
//                if (currentTabIndex == 0) {
//                    // 当前页面如果为聊天历史页面，刷新此页面
//                    if (chatHistoryFragment != null) {
//                        chatHistoryFragment.refresh();
//                    }
//                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(connectionListener);
        }

        if (groupChangeListener != null) {
            EMGroupManager.getInstance().removeGroupChangeListener(groupChangeListener);
        }

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
    }


    /**
     * 连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

            // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
            if (groupSynced && contactSynced) {
                new Thread() {
                    @Override
                    public void run() {
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }
                }.start();
            } else {
                if (!groupSynced) {
                    asyncFetchGroupsFromServer();
                }

                if (!contactSynced) {
                    asyncFetchContactsFromServer();
                }

                if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
                    asyncFetchBlackListFromServer();
                }
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    chatHistoryFragment.errorItem.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {
//                        chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
//                        if (NetUtils.hasNetwork(MainActivity.this))
//                            chatHistoryFragment.errorText.setText(st1);
//                        else
//                            chatHistoryFragment.errorText.setText(st2);

                    }
                }

            });
        }
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
//        updateUnreadAddressLable();
//        // 刷新好友页面ui
//        if (currentTabIndex == 1)
//            contactListFragment.refresh();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
//            updateUnreadLabel();
//            updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginContainerActivity.class));
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginContainerActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    //--chat  上

    //首页菜单
    @InjectView(R.id.icon_menu)
    ImageView icon_menu;

    @OnClick(R.id.icon_menu)
    public void showPopwindow() {
        if (showmenu) {

            content = new String[]{"充值金币", "个人中心", "开始测算"};
            img = new int[]{R.drawable.menu_recharge, R.drawable.item_personal, R.drawable.item_divinate};
            layout = (LinearLayout) getLayoutInflater().inflate(R.layout.action_popuwindow, null);
            mListView = (ListView) layout.findViewById(R.id.lv_popu);
            List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 3; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("tupian", img[i]);
                map.put("neirong", content[i]);
                mList.add(map);
            }
            Log.i("Log", mList.size() + "");
            mListView.setAdapter(new SimpleAdapter(MainActivity.this, mList, R.layout.item_popu, new String[]{"tupian", "neirong"}, new int[]{R.id.show_img, R.id.show_con}));

            pw = new PopupWindow(layout, 316, 316);
            pw.setFocusable(true);
//            ColorDrawable cd = new ColorDrawable(Color.parseColor("#b58043"));
//            pw.setBackgroundDrawable(cd);
            pw.showAsDropDown(icon_menu, 200, 35);
            //点击跳转
            MyClick();
        } else {
            //跳转搜索
            this.startActivity(new Intent(this, SearchContainerActivity.class));
        }
    }


    public void MyClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pw.isShowing()) {
                    pw.dismiss();
                }
                Intent intent = new Intent();
                intent.setClass(mycontext, MeContainerActivity.class);
                if (position == 0) {
                    intent.putExtra(BaseActivity.PAGETO, RechargeFragment.TAG);
                    startActivity(intent);
                } else if (position == 1) {
                    changeItem(3);
                    vp.setCurrentItem(3);
                    toolbar_title_main.setText("个人中心");
                    betterpager();
                } else if (position == 2) {
                    Intent intent1 = new Intent(mycontext, ChatContainerActivity.class);
                    //传参

                    startActivity(intent1);
                }
            }
        });
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        list_frags = new ArrayList<Fragment>();

        home_fragment = new HomePageFragment();
        augur_fragment = new AugurFragment();
        amuse_fragment = new AmuseFragment();
        me_fragment = new MeFragment();

        list_frags.add(home_fragment);
        list_frags.add(augur_fragment);
        list_frags.add(amuse_fragment);
        list_frags.add(me_fragment);

    }

    private void initViews() {
        // TODO Auto-generated method stub
        toolbar_title_main = (TextView) findViewById(R.id.toolbar_title_main);
        vp = (ViewPager) findViewById(R.id.main_vp);
//        vp.setOffscreenPageLimit(1); 不预加载
        tabs[0] = (LinearLayout) findViewById(R.id.main_tab01);
        tabs[1] = (LinearLayout) findViewById(R.id.main_tab02);
        tabs[2] = (LinearLayout) findViewById(R.id.main_tab03);
        tabs[3] = (LinearLayout) findViewById(R.id.main_tab04);
        for (int i = 0; i < 4; i++) {
            tabs[i].setOnClickListener(this);
        }
//        actionbar的切换
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
//            ab.setHomeAsUpIndicator(R.drawable.back);
//            给左上角图标的左边加上一个返回的图标
//            ab.setDisplayHomeAsUpEnabled(true);
            //隐藏icon logo
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
//            ab.setTitle("八字先生");
            toolbar_title_main.setText("八字先生");
        }

    }

    //tabview的点击效果 切换逻辑
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.main_tab01:
                changeItem(0);
                vp.setCurrentItem(0);
                toolbar_title_main.setText("八字排盘");
                icon_menu.setImageResource(R.drawable.icon_menu);
                showmenu = true;
                betterpager();
                break;
            case R.id.main_tab02:
                changeItem(1);
                vp.setCurrentItem(1);
                toolbar_title_main.setText("八字先生");
                icon_menu.setImageResource(R.drawable.menu_search);
                showmenu = false;
                betterpager();
                break;
            case R.id.main_tab03:
                changeItem(2);
                vp.setCurrentItem(2);
                toolbar_title_main.setText("江湖");
                icon_menu.setImageResource(R.drawable.menu_search);
                showmenu = false;
                list_frags.clear();
                initDatas();
                myadapter.setDatas(list_frags);
                break;
            case R.id.main_tab04:
                changeItem(3);
                vp.setCurrentItem(3);
                toolbar_title_main.setText("个人中心");
                icon_menu.setImageResource(R.drawable.icon_menu);
                showmenu = true;
                list_frags.clear();
                initDatas();
                myadapter.setDatas(list_frags);
                break;

            default:
                break;
        }
    }

    /**
     * tabview底部导航栏的状态变化  颜色  文字
     */
    public void changeItem(int pos) {
        for (int i = 0; i < 4; i++) {
            if (pos == i) {
                ((ImageView) tabs[i].getChildAt(0)).setImageResource(imgsel[i]);
                ((TextView) tabs[i].getChildAt(1)).setTextColor(Color.parseColor("#000000"));
            } else {
                ((ImageView) tabs[i].getChildAt(0)).setImageResource(imgnor[i]);
                ((TextView) tabs[i].getChildAt(1)).setTextColor(Color.parseColor("#999999"));
            }
        }
    }

    /**
     * viewpager滑动效果相关
     */

    //
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        changeItem(arg0);
        //viewpager滑动切换标题栏
        toolbar_title_main.setText(titles[arg0]);
        if (arg0 == 1 || arg0 == 2) {
            icon_menu.setImageResource(R.drawable.menu_search);
            showmenu = false;
        } else {
            icon_menu.setImageResource(R.drawable.icon_menu);
            showmenu = true;
        }
        betterpager();
    }

    //fragmentpageradapter优化
    public void betterpager() {
        list_frags.clear();
        initDatas();
        myadapter.setDatas(list_frags);
    }

}
