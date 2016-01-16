package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class ChatRoom extends BmobObject {
    private Chat chat;  //关联一组聊天回话
    private Augur augur; //关联算命先生
    private MyUser myUser;  //关联user表
    private String clientId; //被测算人id
    private String guestId;  //游客id  围观区
    private String roomTime;  //聊天室创建时间 排序用

    //无参构造器
    public ChatRoom() {

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

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getRoomTime() {
        return roomTime;
    }

    public void setRoomTime(String roomTime) {
        this.roomTime = roomTime;
    }

    public ChatRoom(String roomTime, Chat chat, Augur augur, MyUser myUser, String clientId, String guestId) {
        this.roomTime = roomTime;
        this.chat = chat;
        this.augur = augur;
        this.myUser = myUser;
        this.clientId = clientId;
        this.guestId = guestId;
    }
}
