package com.example.administrator.bazipaipan.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.utils.BmobUtils;

/**
 * Created by 王中阳 on 2016/1/7.
 */
public class ChatMainFragment extends Fragment {
    private ChatContainerActivity mycontext;
    private Augur augur;
    private MyUser myUser;
    /**
     * 1进入聊天室（游客围观）
     * 2点击排队（队列中）
     * 3正在测算
     */
    private String chatType;
    /**
     * 1有人排队
     * 2到自己
     */
    private String hasline;

    /**
     * 命主测算状态
     * 1在队列首位 轮到他
     * 2测算中
     * 3测算结束
     */
    private String clientChatType;

    /**
     * 大师测算状态
     * 1测算中
     * 2结束
     */
    private String augurChattype;

    /**
     * 在线状态
     * 1在聊天室
     * 2在app中
     * 3离线状态
     */
    private String onlineType;

    /**
     * 系统消息
     * 1开始测算
     * 2结束测算
     * 3为大师打赏
     */
    private String sysMsg;

    /**
     * 退出聊天室的方式
     * 1点返回 back键及左上角返回按钮退出
     * 2点结束 结束按钮只有大师和命主可见
     */
    private String quitType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userType = BmobUtils.getCurrentUser(mycontext).getType();
        enterRoom(userType);
    }

    private void enterRoom(String type) {
        //大师 非大师进入房间
        chatType = "1";
        switch (type) {
            case "1"://游客
                /**
                 * 1进入就是围观区
                 * 2点击排队之后才能进行聊天
                 * 3一对一测算是从聊天队列中获取的
                 */
                switch (chatType) {
                    case "1"://在围观
                        /**
                         * 1发送信息到围观区，显示简键盘
                         * 2送礼物
                         * 3排队
                         * 4关注大师
                         */
                        //点击排队
                        chatType = "2";
                        break;
                    case "2"://在排队
                        /**
                         * 1在排队队列首位
                         * 2大师点击结束回话按钮
                         * 3填写自己的命盘
                         * 4取消排队
                         */
                        switch (hasline) {
                            case "1"://有人排队

                                //取消排队
                                chatType = "2";
                                break;
                            case "2"://没人排队   到自己
                                chatType = "3";
                                clientChatType = "1";
                                break;
                        }
                        break;
                    case "3"://在测算
                        switch (clientChatType) {
                            case "1"://队列到自己测算 轮到他
                                switch (onlineType) {
                                    case "1"://在聊天室
                                        /**
                                         * 1弹窗提示，到“xxx”测算
                                         * 2显示全键盘：语音 图片 表情
                                         * 3显示发送内容到聊天区
                                         */
                                        sysMsg = "1";
                                        break;
                                    case "2"://在app
                                        /**
                                         * 1弹窗提示到“xxx”
                                         * 2“确定”，进入到大师房间
                                         * 3“取消”，从排队队列中清除，关闭弹窗
                                         */
                                        break;
                                    case "3"://离线
                                        /**
                                         * 1通知栏提醒，点击通知栏进入聊天室
                                         * 2短信提醒  ---是否需要不？？
                                         * 3 1分钟后不回复，从排队队列中清除？？
                                         */
                                        break;
                                }
                                break;
                            case "2"://测算中
                                switch (onlineType) {
                                    case "1"://在聊天室
                                        /**
                                         * 1主动聊天 双方回话
                                         * 2系统消息
                                         */

                                        break;
                                    case "2"://在app
                                        /**
                                         * 提示红点？？
                                         */
                                        break;
                                    case "3"://离线
                                        /**
                                         * 1通知栏提醒，点击通知栏进入聊天室
                                         * 2短信提醒  ---是否需要不？？
                                         * 3 1分钟后不回复，从排队队列中清除？？
                                         */
                                        break;
                                }

                                break;
                            case "3"://测算结束
                                switch (quitType) {
                                    case "1"://back键 左上角退出
                                        /**
                                         * 1弹窗是否退出
                                         */
                                        break;
                                    case "2"://结束按钮退出
                                        /**
                                         * 1提示打赏 弹窗
                                         * 2回到围观区
                                         */

                                        chatType = "2";
                                        break;
                                }

                                break;
                        }
                        break;
                }

                break;
            case "2"://大师
                switch (augurChattype) {
                    case "1"://测算中
                        switch (onlineType) {
                            case "1"://聊天室
                                break;
                            case "2"://在app
                                break;
                            case "3"://离线

                                break;
                        }
                        break;
                    case "2"://结束
                        switch (quitType) {
                            case "1"://点返回 back键
                                /**
                                 * 1测算中：弹窗确定是否退出
                                 * 2休息中：直接退出：队列中没有人排队
                                 * 3添加一种 不测算，但是可以和围观区互动的情景
                                 */
                                break;
                            case "2"://点结束按钮
                                /**
                                 * 1弹窗确定
                                 */
                                break;
                        }
                        break;
                }

                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (ChatContainerActivity) context;
    }
}
