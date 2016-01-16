package com.example.administrator.bazipaipan.utils.changehead;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapUtil {

    public static Bitmap getBitmap(Bitmap bitmap, int i) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, array);
        while (array.toByteArray().length > (i * 1024)) {
            array.reset();
            quality = quality * 90 / 100;
            bitmap.compress(CompressFormat.JPEG, quality, array);
            if (quality < 20) {
                break;
            }
        }
        bitmap = BitmapFactory.decodeByteArray(array.toByteArray(), 0,
                array.toByteArray().length);
        return bitmap;
    }
}
