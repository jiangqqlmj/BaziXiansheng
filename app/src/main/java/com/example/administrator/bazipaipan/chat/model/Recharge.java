package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class Recharge extends BmobObject {
    private ChatRoom chatRoom;
    private MyUser myUser;
    private String clientId;
    private String coinNum;  //金币数量
    private String rechargeTime; //充值时间
    private String rechargeWay; //充值方式 1微信 2支付宝
    private String rechargeStatus; //1成功 2待付款 3失败 4取消
    private String RechargeNum;    //充值金额

    public Recharge() {
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

    public String getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(String coinNum) {
        this.coinNum = coinNum;
    }

    public String getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getRechargeWay() {
        return rechargeWay;
    }

    public void setRechargeWay(String rechargeWay) {
        this.rechargeWay = rechargeWay;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public String getRechargeNum() {
        return RechargeNum;
    }

    public void setRechargeNum(String rechargeNum) {
        RechargeNum = rechargeNum;
    }
}
