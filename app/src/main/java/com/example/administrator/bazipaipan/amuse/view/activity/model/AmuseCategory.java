package com.example.administrator.bazipaipan.amuse.view.activity.model;

import com.example.administrator.bazipaipan.chat.model.Chat;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseCategory extends BmobObject implements Serializable {
    private Chat chatId;
    private String amuseTitle;
    private BmobFile amuseImage;
    private String amuseLookerNum;

    public AmuseCategory() {
    }

    public AmuseCategory(Chat chatId, String amuseTitle, String amuseLookerNum) {
        this.chatId = chatId;
        this.amuseTitle = amuseTitle;
        this.amuseLookerNum = amuseLookerNum;
    }

    //因为构造器的参数不同
    public AmuseCategory(Chat chatId, String amuseTitle, String amuseLookerNum, BmobFile amuseImage) {
        this.chatId = chatId;
        this.amuseTitle = amuseTitle;
        this.amuseImage = amuseImage;
        this.amuseLookerNum = amuseLookerNum;
    }

    public Chat getChatId() {
        return chatId;
    }

    public void setChatId(Chat chatId) {
        this.chatId = chatId;
    }

    public String getAmuseTitle() {
        return amuseTitle;
    }

    public void setAmuseTitle(String amuseTitle) {
        this.amuseTitle = amuseTitle;
    }

    public BmobFile getAmuseImage() {
        return amuseImage;
    }

    public void setAmuseImage(BmobFile amuseImage) {
        this.amuseImage = amuseImage;
    }

    public String getAmuseLookerNum() {
        return amuseLookerNum;
    }

    public void setAmuseLookerNum(String amuseLookerNum) {
        this.amuseLookerNum = amuseLookerNum;
    }

    @Override
    public String toString() {
        return "AmuseCategory{" +
                ", chatId='" + chatId + '\'' +
                ", amuseTitle='" + amuseTitle + '\'' +
                ", amuseImage=" + amuseImage +
                ", amuseLookerNum='" + amuseLookerNum + '\'' +
                '}';
    }
}
