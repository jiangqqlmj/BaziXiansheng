package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseDetail;
import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2015/12/25.
 */
public class Chat extends BmobObject {
    //关联
    private MyUser myUser_pointer;
    private Augur augur_pointer;
    private AmuseDetail amuseDetail_pointer;

    //标记位
    private String augurId;
    private String clientId;
    private boolean chatStatus;
    private String chatMessageType;  //1文字 2图片 3语音
    //内容
    private String chatMessage;
    private String chatTime;

    public MyUser getMyUser_pointer() {
        return myUser_pointer;
    }

    public void setMyUser_pointer(MyUser myUser_pointer) {
        this.myUser_pointer = myUser_pointer;
    }

    public Augur getAugur_pointer() {
        return augur_pointer;
    }

    public void setAugur_pointer(Augur augur_pointer) {
        this.augur_pointer = augur_pointer;
    }

    public AmuseDetail getAmuseDetail_pointer() {
        return amuseDetail_pointer;
    }

    public void setAmuseDetail_pointer(AmuseDetail amuseDetail_pointer) {
        this.amuseDetail_pointer = amuseDetail_pointer;
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

    public boolean isChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(boolean chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(String chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
