package com.example.administrator.bazipaipan.chat.receiver;


import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.example.administrator.bazipaipan.MyApplication;

import java.util.UUID;

/**
 * Created by 王中阳 on 2015/12/31.
 */
public class MyGroupChangeListener implements GroupChangeListener {

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

        //收到加入群聊的邀请

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
        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        msg.setChatType(EMMessage.ChatType.GroupChat);
        msg.setFrom(inviter);
        msg.setTo(groupId);
        msg.setMsgId(UUID.randomUUID().toString());
        msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
        // 保存邀请消息
        EMChatManager.getInstance().saveMessage(msg);
        // 提醒新消息
        EMNotifier.getInstance(MyApplication.getInstance()).notifyOnNewMsg();

    }

    @Override
    public void onInvitationAccpted(String groupId, String inviter,
                                    String reason) {
        //群聊邀请被接受
    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee,
                                     String reason) {
        //群聊邀请被拒绝
    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {
        //当前用户被管理员移除出群聊

    }

    @Override
    public void onGroupDestroy(String groupId, String groupName) {
        //群聊被创建者解散
        // 提示用户群被解散

    }

    @Override
    public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
        // 用户申请加入群聊，收到加群申请
    }

    @Override
    public void onApplicationAccept(String groupId, String groupName, String accepter) {
        // 加群申请被同意
        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        msg.setChatType(EMMessage.ChatType.GroupChat);
        msg.setFrom(accepter);
        msg.setTo(groupId);
        msg.setMsgId(UUID.randomUUID().toString());
        msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
        // 保存同意消息
        EMChatManager.getInstance().saveMessage(msg);
        // 提醒新消息
        EMNotifier.getInstance(MyApplication.getInstance()).notifyOnNewMsg();
    }

    @Override
    public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
        // 加群申请被拒绝
    }

}