package com.example.administrator.bazipaipan.augur.model;

import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class Augur extends BmobObject {
    private String userId;
    private boolean divinateStatus;
    private int lookerNum;
    private String accuracy;
    private int focusNum;
    private String focusFans;
    private String divinatedNum;
    //    private String goldNum;
    //关联关系
    private MyUser augur_pointer;
    //关联环信的群聊id
    private String roomId;



    public boolean isDivinateStatus() {
        return divinateStatus;
    }

    public void setDivinateStatus(boolean divinateStatus) {
        this.divinateStatus = divinateStatus;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Augur() {
    }

    public MyUser getAugur_pointer() {
        return augur_pointer;
    }

    public void setAugur_pointer(MyUser augur_pointer) {
        this.augur_pointer = augur_pointer;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLookerNum() {
        return lookerNum;
    }

    public void setLookerNum(int lookerNum) {
        this.lookerNum = lookerNum;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public String getFocusFans() {
        return focusFans;
    }

    public void setFocusFans(String focusFans) {
        this.focusFans = focusFans;
    }

    public String getDivinatedNum() {
        return divinatedNum;
    }

    public void setDivinatedNum(String divinatedNum) {
        this.divinatedNum = divinatedNum;
    }

//    public String getGoldNum() {
//        return goldNum;
//    }
//
//    public void setGoldNum(String goldNum) {
//        this.goldNum = goldNum;
//    }
}
