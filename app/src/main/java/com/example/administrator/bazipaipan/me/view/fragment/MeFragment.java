package com.example.administrator.bazipaipan.me.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.MyActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.utils.BmobUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MeFragment";
    private MainActivity mycontext;

    //控件
    ImageView user_head;
    TextView tv_user_name, tv_gold_num;
    Button btn_recharge, btn_beaugur;
    RelativeLayout container_myfocus, container_editinfo, container_setting, container_tochat;
    //重复注册大师
    Augur augur;


    public MeFragment() {
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        initDatas();
        identityJudge();

    }

    //聊天对话框显示
    private void identityJudge() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, MyUser.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.getObject(mycontext, BmobUser.getCurrentUser(mycontext).getObjectId(), new GetListener<MyUser>() {

            @Override
            public void onSuccess(MyUser object) {
                // TODO Auto-generated method stub
                if (object.getType().equals("2")) {
                    container_tochat.setVisibility(View.VISIBLE);
                    btn_beaugur.setVisibility(View.GONE);
                } else {
                    container_tochat.setVisibility(View.GONE);
//                    btn_beaugur.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(int code, String arg0) {


            }

        });
    }

    private void judgeAugur() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.getObject(mycontext, BmobUser.getCurrentUser(mycontext).getObjectId(), new GetListener<MyUser>() {

            @Override
            public void onSuccess(MyUser object) {
                // TODO Auto-generated method stub
                if (object.getType().equals("2")) {
                    //不能重复申请成为大师
                    mycontext.toast("您已经是大师，不能重复申请");
                    Log.e("datas", "MeFragment 判断type" + object.getType());
                    btn_beaugur.setClickable(false);

                }

            }

            @Override
            public void onFailure(int code, String arg0) {


            }

        });
        initDatas();
    }

    //为控件赋初值
    private void initDatas() {
        //BmobUser中的特定属性
        int gold_num;
        String username = (String) BmobUser.getObjectByKey(mycontext, "username");
        //MyUser中的扩展属性
        String user_type = (String) BmobUser.getObjectByKey(mycontext, "type");
        BmobUtils.log("meFragment's usertype:" + user_type);
        if (BmobUser.getObjectByKey(mycontext, "goldNum") != null) {
            gold_num = (int) BmobUser.getObjectByKey(mycontext, "goldNum");
        } else {
            gold_num = 0;
        }
        if (TextUtils.isEmpty(username)) {
            tv_user_name.setText("");  //电话号码
        } else {
            tv_user_name.setText(username);  //电话号码
        }
        //根据用户类别判断：
        if (user_type == null) {
            user_head.setImageResource(R.drawable.guest_head_me);
            return;
        }
        if (user_type.equals("1")) {
            user_head.setImageResource(R.drawable.guest_head_me);
        } else if (user_type.equals("2")) {
            user_head.setImageResource(R.drawable.augur_head_me);
        }
        if (TextUtils.isEmpty(gold_num + "")) {
            tv_gold_num.setText("0");
        } else {
            tv_gold_num.setText(gold_num + "");
        }

    }

    private void initviews() {
        //成为大师
        btn_beaugur = (Button) mycontext.findViewById(R.id.btn_me_beaugur);
        btn_beaugur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeAugur();
            }
        });
        user_head = (ImageView) mycontext.findViewById(R.id.iv_me_my_head);
        //网络中请求
        BmobUtils.getCurrentUser(mycontext).getAvatar().loadImageThumbnail(mycontext, user_head, 160, 160, 100);
        tv_user_name = (TextView) mycontext.findViewById(R.id.tv_me_user_name);
        tv_gold_num = (TextView) mycontext.findViewById(R.id.tv_me_my_goldnum);
        btn_recharge = (Button) mycontext.findViewById(R.id.btn_me_recharge);
        btn_recharge.setOnClickListener(this);
        container_myfocus = (RelativeLayout) mycontext.findViewById(R.id.container_myfocus);
        container_myfocus.setOnClickListener(this);
        container_editinfo = (RelativeLayout) mycontext.findViewById(R.id.container_editdata);
        container_editinfo.setOnClickListener(this);
        container_setting = (RelativeLayout) mycontext.findViewById(R.id.container_setting);
        container_setting.setOnClickListener(this);
        container_tochat = (RelativeLayout) mycontext.findViewById(R.id.container_tochat);
        container_tochat.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_me, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

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

    //点击事件
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_me_recharge:
                intent.setClass(getActivity(), MeContainerActivity.class);
                intent.putExtra(MyActivity.PAGETO, RechargeFragment.TAG);
                break;
            case R.id.container_myfocus:
                intent.setClass(getActivity(), MeContainerActivity.class);
                intent.putExtra(MyActivity.PAGETO, MyFocusFragment.TAG);
                break;
            case R.id.container_editdata:
                intent.setClass(getActivity(), MeContainerActivity.class);
                intent.putExtra(MyActivity.PAGETO, EditInfoFragment.TAG);
                break;
            case R.id.container_setting:
                intent.setClass(getActivity(), MeContainerActivity.class);
                intent.putExtra(MyActivity.PAGETO, SettingFragment.TAG);
                break;
            //大师身份聊天
            case R.id.container_tochat:
                intent.setClass(getActivity(), ChatContainerActivity.class);
                break;
            default:
                break;
        }

        //抽取出跳转方法  以上设置传递的数据
        startActivity(intent);
    }
}
