package com.example.administrator.bazipaipan.login.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 王中阳 on 2015/12/22.
 */
public class MyUser extends BmobUser {
    private String type;
    private String nick;
    private String verificationCode;
    private String sex;
    private String birthday;
    private String birthtime;
    private String dateType;  //1阳历  2阴历
    private BmobFile avatar; //头像
    private String city;
    private String sign;
    private int goldNum;
    private Boolean onlineStatus;  //在线状态  上线通知用
    //    聊天相关
    private String isCreatedGroup; //1没创建聊天室  2创建了聊天室

    public String getIsCreatedGroup() {
        return isCreatedGroup;
    }

    public void setIsCreatedGroup(String isCreatedGroup) {
        this.isCreatedGroup = isCreatedGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public MyUser(String type, String nick, String verificationCode, String mobilePhoneNumber, String sex, String birthday, String birthtime, BmobFile avatar, String city, String sign, int goldNum, Boolean onlineStatus) {
        this.type = type;
        this.nick = nick;
        this.verificationCode = verificationCode;
        this.sex = sex;
        this.birthday = birthday;
        this.birthtime = birthtime;
        this.avatar = avatar;
        this.city = city;
        this.sign = sign;
        this.goldNum = goldNum;
        this.onlineStatus = onlineStatus;
    }

    public MyUser() {
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "type='" + type + '\'' +
                ", nick='" + nick + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", birthtime='" + birthtime + '\'' +
                ", avatar=" + avatar +
                ", city='" + city + '\'' +
                ", sign='" + sign + '\'' +
                ", goldNum='" + goldNum + '\'' +
                ", onlineStatus=" + onlineStatus +
                '}';
    }
}
