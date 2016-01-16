package com.example.administrator.bazipaipan.chat;

import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.NetUtils;
import com.example.administrator.bazipaipan.MyApplication;

/**
 * Created by 王中阳 on 2015/12/29.
 */
public class ChatUtils {
    //    注册
    public static void chatRegister(String username, String pwd) {

    }

    //    登陆聊天服务器 需要注意： 登陆成功后需要调用
    public static void loadAllGroups() {
        EMGroupManager.getInstance().loadAllGroups();
    }

    //    从本地数据库加载群组到内存的操作，如果你的应用中有群组，请加上这句话（要求在每次进入应用的时候调用）
    public static void loadAllConversations() {
        EMChatManager.getInstance().loadAllConversations();
    }

    //    登录聊天室
    public static void loginChatRoom() {
//        EMChatManager.getInstance().login(userName, password, new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        EMGroupManager.getInstance().loadAllGroups();
//                        EMChatManager.getInstance().loadAllConversations();
//                        Log.d("main", "登陆聊天服务器成功！");
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.d("main", "登陆聊天服务器失败！");
//            }
//        });

    }

    //    重连
    public void AgainConnection() {
//注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            //已连接到服务器
        }

        @Override
        public void onDisconnected(final int error) {
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (error == EMError.USER_REMOVED) {
//                        // 显示帐号已经被移除
//                    } else if (error == EMError.CONNECTION_CONFLICT) {
//                        // 显示帐号在其他设备登陆
//                    } else {
//                        if (NetUtils.hasNetwork(MyApplication.getInstance().getContext())) {
//                            //连接不到聊天服务器
//
//                        } else {
//                            //当前网络不可用，请检查网络设置
//
//                        }
//                    }
//                }
//            });
        }
    }

    //    退出聊天
    public static void chatLogout() {
        EMChatManager.getInstance().logout();
    }
}
