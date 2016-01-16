package com.example.administrator.bazipaipan.chat.model;


import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class GiveGift extends BmobObject {
    private ChatRoom chatRoom;
    private Gift gift;
    private String augurId;
    private String clientId;
    private String giveTime;

    public GiveGift() {
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public String getAugurId() {
        return augurId;
    }

    public void setAugurId(String augurId) {
        this.augurId = augurId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(String giveTime) {
        this.giveTime = giveTime;
    }
}
