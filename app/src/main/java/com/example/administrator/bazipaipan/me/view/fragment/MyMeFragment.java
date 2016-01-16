//package com.example.administrator.bazipaipan.me.view.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.administrator.bazipaipan.MyActivity;
//import com.example.administrator.bazipaipan.R;
//import com.example.administrator.bazipaipan.me.view.activity.MeContainerActivity;
//
//import cn.bmob.v3.BmobUser;
//
///**
// * Created by 王中阳 on 2015/12/16.
// */
//public class MyMeFragment extends Fragment implements View.OnClickListener {
//    public static final String TAG = "MyMeFragment";
//    private MeContainerActivity mycontext;
//
//    //控件
//    ImageView user_head;
//    TextView tv_user_name, tv_gold_num;
//    Button btn_recharge;
//    RelativeLayout container_myfocus, container_editinfo, container_setting;
//
//    public MyMeFragment() {
//
//    }
//
//    public static MyMeFragment newInstance() {
//        MyMeFragment fragment = new MyMeFragment();
//        return fragment;
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.mycontext = (MeContainerActivity) context;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initviews();
//        initDatas();
//    }
//
//    //为控件赋初值
//    private void initDatas() {
//        //BmobUser中的特定属性
//        String username = (String) BmobUser.getObjectByKey(mycontext, "username");
//        //MyUser中的扩展属性
//        String user_type = (String) BmobUser.getObjectByKey(mycontext, "type");
//        mycontext.toast(user_type + "type");
//        String gold_num = (String) BmobUser.getObjectByKey(mycontext, "goldNum");
//        if (TextUtils.isEmpty(username)) {
//            tv_user_name.setText("");  //电话号码
//        } else {
//            tv_user_name.setText(username);  //电话号码
//        }
//        //根据用户类别判断：
//        if (user_type == null) {
//            user_head.setImageResource(R.drawable.guest_head_me);
//            return;
//        }
//        if (user_type.equals("1")) {
//            user_head.setImageResource(R.drawable.guest_head_me);
//        } else if (user_type.equals("2")) {
//            user_head.setImageResource(R.drawable.augur_head_me);
//        }
//        if (TextUtils.isEmpty(gold_num)) {
//            tv_gold_num.setText("0");
//        } else {
//            tv_gold_num.setText(gold_num);
//        }
//
//    }
//
//    private void initviews() {
//        user_head = (ImageView) mycontext.findViewById(R.id.iv_me_my_head);
//        tv_user_name = (TextView) mycontext.findViewById(R.id.tv_me_user_name);
//        tv_gold_num = (TextView) mycontext.findViewById(R.id.tv_me_my_goldnum);
//        btn_recharge = (Button) mycontext.findViewById(R.id.btn_me_recharge);
//        btn_recharge.setOnClickListener(this);
//        container_myfocus = (RelativeLayout) mycontext.findViewById(R.id.container_myfocus);
//        container_myfocus.setOnClickListener(this);
//        container_editinfo = (RelativeLayout) mycontext.findViewById(R.id.container_editdata);
//        container_editinfo.setOnClickListener(this);
//        container_setting = (RelativeLayout) mycontext.findViewById(R.id.container_setting);
//        container_setting.setOnClickListener(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.content_me, null);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    //点击事件
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.btn_me_recharge:
//                intent.setClass(getActivity(), MeContainerActivity.class);
//                intent.putExtra(MyActivity.PAGETO, RechargeFragment.TAG);
//                break;
//            case R.id.container_myfocus:
//                intent.setClass(getActivity(), MeContainerActivity.class);
//                intent.putExtra(MyActivity.PAGETO, MyFocusFragment.TAG);
//                break;
//            case R.id.container_editdata:
//                intent.setClass(getActivity(), MeContainerActivity.class);
//                intent.putExtra(MyActivity.PAGETO, EditInfoFragment.TAG);
//                break;
//            case R.id.container_setting:
//                intent.setClass(getActivity(), MeContainerActivity.class);
//                intent.putExtra(MyActivity.PAGETO, SettingFragment.TAG);
//                break;
//            default:
//                break;
//        }
//
//        //抽取出跳转方法  以上设置传递的数据
//        startActivity(intent);
//    }
//}
