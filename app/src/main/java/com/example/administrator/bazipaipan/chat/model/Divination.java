package com.example.administrator.bazipaipan.chat.model;

import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.login.model.MyUser;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class Divination extends BmobObject {
    private MyUser myUser;
    private Augur augur;
    private String clientId;
    private String name;
    private String sex;
    private String dateType; //1阳历 2阴历
    private String birthday;
    private String birthtime;
    private Date divinateTime;  //排盘时间

    public Divination() {
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public Augur getAugur() {
        return augur;
    }

    public void setAugur(Augur augur) {
        this.augur = augur;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public Date getDivinateTime() {
        return divinateTime;
    }

    public void setDivinateTime(Date divinateTime) {
        this.divinateTime = divinateTime;
    }
}
