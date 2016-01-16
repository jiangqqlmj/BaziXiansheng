package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class ChatContribute extends BmobObject {
    private ChatRoom chatRoom;  //关联聊天室
    private MyUser myUser;
    private String clientId;    //贡献人id
    private String contributeNum; //金币数量

    public ChatContribute() {
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContributeNum() {
        return contributeNum;
    }

    public void setContributeNum(String contributeNum) {
        this.contributeNum = contributeNum;
    }
}
