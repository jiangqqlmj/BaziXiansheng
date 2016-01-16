package com.example.administrator.bazipaipan.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;

/**
 * 进度条工具类
 */
public class ProgressUtil {

    private static Dialog loadingDialog;

    public static Dialog showProgressWithMsg(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v;
        if (Build.VERSION.SDK_INT < 14) {
            v = inflater.inflate(R.layout.progressbar_lay, null);// 得到加载view
        } else {
            v = inflater.inflate(R.layout.layout_progress, null);// 得到加载view
        }
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.progress_tv);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        loadingDialog.show();
        return loadingDialog;
    }

    public static void closeProgress() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
