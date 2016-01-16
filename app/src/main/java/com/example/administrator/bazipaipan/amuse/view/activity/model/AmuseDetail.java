package com.example.administrator.bazipaipan.amuse.view.activity.model;

import com.example.administrator.bazipaipan.chat.model.Chat;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseDetail extends BmobObject{
    private String amuseIntro;
    private String amuseCommentNum;
    private String amuseDetailTitle;
    //关联主键
    private AmuseCategory amuseCategory_pointer;
    private Chat chat_pointer;

    public AmuseDetail(String amuseIntro, String amuseCommentNum, AmuseCategory amuseCategory_pointer, Chat chat_pointer) {
        this.amuseIntro = amuseIntro;
        this.amuseCommentNum = amuseCommentNum;
        this.amuseCategory_pointer = amuseCategory_pointer;
        this.chat_pointer = chat_pointer;
    }

    public AmuseDetail() {
    }

    public String getAmuseDetailTitle() {
        return amuseDetailTitle;
    }

    public void setAmuseDetailTitle(String amuseDetailTitle) {
        this.amuseDetailTitle = amuseDetailTitle;
    }

    public String getAmuseIntro() {
        return amuseIntro;
    }

    public void setAmuseIntro(String amuseIntro) {
        this.amuseIntro = amuseIntro;
    }

    public String getAmuseCommentNum() {
        return amuseCommentNum;
    }

    public void setAmuseCommentNum(String amuseCommentNum) {
        this.amuseCommentNum = amuseCommentNum;
    }

    public AmuseCategory getAmuseCategory_pointer() {
        return amuseCategory_pointer;
    }

    public void setAmuseCategory_pointer(AmuseCategory amuseCategory_pointer) {
        this.amuseCategory_pointer = amuseCategory_pointer;
    }

    public Chat getChat_pointer() {
        return chat_pointer;
    }

    public void setChat_pointer(Chat chat_pointer) {
        this.chat_pointer = chat_pointer;
    }

    @Override
    public String toString() {
        return "AmuseDetail{" +
                "amuseIntro='" + amuseIntro + '\'' +
                ", amuseCommentNum='" + amuseCommentNum + '\'' +
                ", amuseCategory_pointer=" + amuseCategory_pointer +
                ", chat_pointer=" + chat_pointer +
                '}';
    }
}
