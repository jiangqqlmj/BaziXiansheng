package com.example.administrator.bazipaipan.chat.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 王中阳 on 2016/1/3.
 */
public class Gift extends BmobObject {
    private BmobFile giftImage;
    private String giftName;
    private String giftPrice;

    public Gift() {
    }

    public BmobFile getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(BmobFile giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }
}
