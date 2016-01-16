package com.example.administrator.bazipaipan.me.view.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.login.LoginContainerActivity;
import com.example.administrator.bazipaipan.me.view.model.UpdataInfo;
import com.example.administrator.bazipaipan.utils.DownLoadManager;
import com.example.administrator.bazipaipan.utils.UpdataInfoParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateVersionActivity extends MyActivity {
    private LoginContainerActivity fragActivity;
    // 版本更新
    private final String TAG_VERSION = this.getClass().getName();
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int DOWN_ERROR = 4;
    private UpdataInfo info;
    private String localVersion;

    public static final String TAG = "AboutUpFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //相同的布局文件  和设置页
        setContentView(R.layout.content_setting);
        // 版本更新
        updateVersion();
    }

    private void updateVersion() {
        // TODO Auto-generated method stub
        try {
            localVersion = getVersionName();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CheckVersionTask cv = new CheckVersionTask();
        new Thread(cv).start();

    }

    private String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = fragActivity.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(
                "com.example.administrator.bazipaipan", 0);
        return packInfo.versionName;
    }

    public class CheckVersionTask implements Runnable {
        InputStream is;

        public void run() {
            try {
                String path = "http://www.up1024.com/strings.xml"; // 服务器端地址
                // String path = "http://www.up1024.com/test/strings.xml";//测试连接
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                localVersion = "1.1.0";
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG_VERSION, "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATA_NONEED;
                    handler.sendMessage(msg);
                    // LoginMain();
                } else if (!info.getVersion().equals(localVersion)) {
                    Log.i(TAG_VERSION, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
                    Toast.makeText(getApplicationContext(), "当前为最新版本",
                            Toast.LENGTH_SHORT).show();
                    UpdateVersionActivity.this.finish();
                    break;
                case UPDATA_CLIENT:
                    // 对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    // 服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_LONG)
                            .show();
                    UpdateVersionActivity.this.finish();
                    break;
                case DOWN_ERROR:

                    // 下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    UpdateVersionActivity.this.finish();
                    break;
            }
        }
    };

    /*
     * * 弹出对话框通知用户更新程序 * 弹出对话框的步骤： * 1.创建alertDialog的builder. * 2.要给builder设置属性,
     * 对话框的内容,样式,按钮 * 3.通过builder 创建一个对话框 * 4.对话框show()出来
     */
    protected void showUpdataDialog() {
        Builder builer = new Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        // 当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG_VERSION, "下载apk,更新");
                downLoadApk();
            }

            /* * 从服务器中下载APK */
            private void downLoadApk() {
                // TODO Auto-generated method stub
                final ProgressDialog pd;
                // //进度条对话框
                pd = new ProgressDialog(UpdateVersionActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("正在下载更新");
                pd.show();
                pd.setCanceledOnTouchOutside(false);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            File file = DownLoadManager.getFileFromServer(
                                    info.getUrl(), pd);
                            installApk(file);
                            pd.dismiss(); // 结束掉进度条对话框

                            UpdateVersionActivity.this.finish();// 关闭更新界面
                        } catch (Exception e) {
                            Message msg = new Message();
                            msg.what = DOWN_ERROR;
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // 回退到关于顶格的fragment
                UpdateVersionActivity.this.finish();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
