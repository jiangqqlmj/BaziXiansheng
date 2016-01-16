package com.example.administrator.bazipaipan.chat.model;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by 王中阳 on 2016/1/13.
 */
public class MyChatMsg implements Serializable {
    private Image avator;
    private String nick;
    private String msg;

    public Image getAvator() {
        return avator;
    }

    public void setAvator(Image avator) {
        this.avator = avator;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
