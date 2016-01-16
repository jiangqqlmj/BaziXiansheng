package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class ChatQueue extends BmobObject {
    private Chat chat;
    private Augur augur;    //和房间二者选一
    private ChatRoom chatRoom;
    //排队的队列  list
    private MyUser myUser;
    private String queueNum;
    private String queueTime;

    public ChatQueue() {
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Augur getAugur() {
        return augur;
    }

    public void setAugur(Augur augur) {
        this.augur = augur;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public String getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(String queueNum) {
        this.queueNum = queueNum;
    }

    public String getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(String queueTime) {
        this.queueTime = queueTime;
    }
}
