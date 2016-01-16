package com.example.administrator.bazipaipan.homepage.view.fragment.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 王中阳 on 2015/12/18.
 */
public class NewsAmuse extends BmobObject {
    private String title;
    String introduce;

    public NewsAmuse(String title, String introduce) {
        this.title = title;
        this.introduce = introduce;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
