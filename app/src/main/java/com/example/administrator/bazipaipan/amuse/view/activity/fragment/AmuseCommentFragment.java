package com.example.administrator.bazipaipan.amuse.view.activity.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.activity.AmuseContainerActivity;
import com.example.administrator.bazipaipan.amuse.view.activity.adapter.AmuseCommentAdapter;
import com.example.administrator.bazipaipan.amuse.view.activity.model.ChatComment;
import com.example.administrator.bazipaipan.augur.adapter.AugurAdapter;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseCommentFragment extends Fragment implements AugurAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TextView.OnEditorActionListener {
    public static final String TAG = "AmuseCommentFragment";
    //1context
    private AmuseContainerActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<ChatComment> datas;
    //4适配器
    private AmuseCommentAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6分享
    private ImageView iv_share;
    //评论跳转
    private TextView comment_num;
    //---------01.07
    private ImageView iv_back;
    private TextView tv_comment_num;
    private EditText et_my_comment;

    public static AmuseCommentFragment newInstance() {
        AmuseCommentFragment fragment = new AmuseCommentFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (AmuseContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        //接收上一级传递的intent数据还没有写
    }

    private void initviews() {
        //进行评论
        et_my_comment = (EditText) mycontext.findViewById(R.id.et_my_comment);
        et_my_comment.setOnEditorActionListener(this);

        iv_share = (ImageView) mycontext.findViewById(R.id.iv_share_btn);
        iv_share.setOnClickListener(this);

        comment_num = (TextView) mycontext.findViewById(R.id.tv_comment_num);
        comment_num.setText("0" + "人评论");
        comment_num.setOnClickListener(this);

        iv_back = (ImageView) mycontext.findViewById(R.id.amuse_back);
        iv_back.setOnClickListener(this);

    }

    //评论
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //监听键盘发送功能
        String sendmsg;
        sendmsg = et_my_comment.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (TextUtils.isEmpty(sendmsg)) {
                mycontext.toast("请输入内容");
                return false;
            } else {
                mycontext.toast("发送：" + sendmsg);
                et_my_comment.setText("");
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_comment_share, container, false);
        updateUI(view);
        return view;
    }

    //做成girdview的样式
    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //适配器未装填内容
        mAdapter = new AmuseCommentAdapter(mycontext);
        recyclerView.setAdapter(mAdapter);
        //滑动加载更多的layout
        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright);
        //需要实现方法
        mSwipeLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        updateFromNet();
    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //抓取数据

        mHandler.sendEmptyMessage(0);
        mSwipeLayout.setRefreshing(false);

    }

    //点击item的跳转逻辑
    @Override
    public void onItemClicked(int position) {


    }

    //刷新数据相关
    public void setSwipeToRefreshEnabled(boolean enabled) {
        mSwipeLayout.setEnabled(enabled);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mSwipeLayout.isRefreshing()) {
                        mSwipeLayout.setRefreshing(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //点击按钮
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_share_btn:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.up1024.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.tv_comment_num:
                Intent intent = new Intent(mycontext, AmuseContainerActivity.class);
                //还需要传递关联关系
                intent.putExtra(BaseActivity.PAGETO, AmuseCommentDetailFragment.TAG);
                mycontext.startActivity(intent);
                break;

            case R.id.amuse_back:
                mycontext.finish();


        }
    }

    /**
     * 获得图片Uri集合
     * 分享相关
     */
    private ArrayList<Uri> getUriListForImages() {
        ArrayList<Uri> myList = new ArrayList<Uri>();
        String imageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File imageDirectory = new File(imageDirectoryPath);
        String[] fileList = imageDirectory.list();
        if (fileList.length != 0) {
            for (int i = 0; i < 5; i++) {
                try {
                    ContentValues values = new ContentValues(7);
                    values.put(MediaStore.Images.Media.TITLE, fileList[i]);
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileList[i]);
                    values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.ImageColumns.BUCKET_ID, imageDirectoryPath.hashCode());
                    values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, fileList[i]);
                    values.put("_data", imageDirectoryPath + fileList[i]);
                    ContentResolver contentResolver = mycontext.getContentResolver();
                    Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    myList.add(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return myList;
    }
}
