package com.example.administrator.bazipaipan.me.view.model;

import com.example.administrator.bazipaipan.chat.model.Chat;
import com.example.administrator.bazipaipan.login.model.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/1/11.
 */
public class Recharge extends BmobObject {
    //    private Chat chatPointer;
//    private MyUser myUserPoinet;
//    private String rechargeWay;
//    private String rechargeStatus; //支付状态
    private int coinNum;
    private float rechargeNum;

    public Recharge() {
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public float getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(int rechargeNum) {
        this.rechargeNum = rechargeNum;
    }
}
