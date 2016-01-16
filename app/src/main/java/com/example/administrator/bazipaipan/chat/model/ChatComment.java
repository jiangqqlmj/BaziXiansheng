package com.example.administrator.bazipaipan.chat.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class ChatComment extends BmobObject {
    private Chat chat; //关联一组聊天 一哥chatRoom内可以有多组chat
    private String commentMessage;
    private String commentTime;  //评论时间
    private boolean commentOver;    //测算状态 结束与否

    public ChatComment() {
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public boolean isCommentOver() {
        return commentOver;
    }

    public void setCommentOver(boolean commentOver) {
        this.commentOver = commentOver;
    }
}
