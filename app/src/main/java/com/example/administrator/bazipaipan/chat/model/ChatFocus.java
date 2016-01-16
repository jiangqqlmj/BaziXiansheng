package com.example.administrator.bazipaipan.chat.model;


import com.example.administrator.bazipaipan.augur.model.Augur;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class ChatFocus extends BmobObject {
    //关联
    private Augur augur;
    private ChatRoom chatRoom;

    private String augurId;
    private String clientId;
    private String focusTime;

    public ChatFocus() {
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

    public String getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(String focusTime) {
        this.focusTime = focusTime;
    }
}
