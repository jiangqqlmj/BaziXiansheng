package com.example.administrator.bazipaipan.amuse.view.activity.model;

import com.example.administrator.bazipaipan.chat.model.Chat;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class ChatComment extends BmobObject implements Serializable {

    private String commnetMessage;
    private String commentTime;
    private boolean commentStatus;
    //关联
    private Chat chat_pointer;

    public ChatComment(String commnetMessage, String commentTime, boolean commentStatus, Chat chat_pointer) {
        this.commnetMessage = commnetMessage;
        this.commentTime = commentTime;
        this.commentStatus = commentStatus;
        this.chat_pointer = chat_pointer;
    }

    public String getCommnetMessage() {
        return commnetMessage;
    }

    public void setCommnetMessage(String commnetMessage) {
        this.commnetMessage = commnetMessage;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public boolean isCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(boolean commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Chat getChat_pointer() {
        return chat_pointer;
    }

    public void setChat_pointer(Chat chat_pointer) {
        this.chat_pointer = chat_pointer;
    }

    @Override
    public String toString() {
        return "ChatComment{" +
                "commnetMessage='" + commnetMessage + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", commentStatus=" + commentStatus +
                ", chat_pointer=" + chat_pointer +
                '}';
    }
}
