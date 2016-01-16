//package com.example.administrator.bazipaipan.login.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.EMCallBack;
//import com.easemob.EMError;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMGroupManager;
//import com.easemob.exceptions.EaseMobException;
//import com.example.administrator.bazipaipan.MainActivity;
//import com.example.administrator.bazipaipan.MyActivity;
//import com.example.administrator.bazipaipan.MyApplication;
//import com.example.administrator.bazipaipan.R;
//import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseDetailFragment;
//import com.example.administrator.bazipaipan.chat.receiver.MyGroupChangeListener;
//import com.example.administrator.bazipaipan.login.LoginContainerActivity;
//import com.example.administrator.bazipaipan.login.model.MyUser;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//import cn.bmob.v3.BmobSMS;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.RequestSMSCodeListener;
//import cn.bmob.v3.listener.SaveListener;
//import cn.bmob.v3.listener.UpdateListener;
//
///**
// * Created by 王中阳 on 2015/12/21.
// */
//public class RegisterFragment extends Fragment implements View.OnClickListener {
//    public static final String TAG = "RegisterFragment";
//    private LoginContainerActivity mycontext;
//
//    //控件
//    EditText input_phone, input_password, input_verification;
//    Button btn_getverification;
//    RadioButton rb_guest, rb_augur;
//    //注册
//    BmobUser user = new BmobUser();
//    private String phone_num, password, verification, user_type, bmobObjectId;
//    Button btn_login, btn_login_sign;
//    //聊天相关
//    //groupName：要创建的群聊的名称
//    //desc：群聊简介
//    private String groupName, desc;
//    //新登录
//
//    public RegisterFragment() {
//
//    }
//
//    public static RegisterFragment newInstance() {
//        RegisterFragment fragment = new RegisterFragment();
//        return fragment;
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.mycontext = (LoginContainerActivity) context;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initviews();
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View mRoot = inflater.inflate(R.layout.content_login, null);
//        ButterKnife.inject(this, mRoot);
//        return mRoot;
//    }
//
//
//    private void initviews() {
////        EditText input_phone, input_password, input_verification;
////        Button btn_getverification;
////        RadioButton rb_guest, rb_augur;
//        input_phone = (EditText) mycontext.findViewById(R.id.et_input_phone_num);
//        input_password = (EditText) mycontext.findViewById(R.id.et_input_password);
//        input_verification = (EditText) mycontext.findViewById(R.id.et_input_verification);
//        btn_getverification = (Button) mycontext.findViewById(R.id.btn_get_verification);
//        btn_getverification.setOnClickListener(this);
//        rb_guest = (RadioButton) mycontext.findViewById(R.id.rb_guest);
//        rb_guest.setOnClickListener(this);
//        rb_augur = (RadioButton) mycontext.findViewById(R.id.rb_augur);
//        rb_augur.setOnClickListener(this);
//        btn_login = (Button) mycontext.findViewById(R.id.btn_login);
//        btn_login.setOnClickListener(this);
//        btn_login_sign = (Button) mycontext.findViewById(R.id.btn_login_sign);
//        btn_login_sign.setOnClickListener(this);
//
//    }
//
//    //注册
//    @InjectView(R.id.tv_register)
//    TextView textView;
//
//    @OnClick(R.id.tv_register)
//    public void register() {
//        Intent intent = new Intent(mycontext, LoginContainerActivity.class);
////        AmuseCategory bean = list.get(position);
////            intent.putExtra(AmuseFragment.EXTRAL_DATA, bean);
//        intent.putExtra(MyActivity.PAGETO, AmuseDetailFragment.TAG);
//        mycontext.startActivity(intent);
//
//    }
//
//    //点击事件
//    @Override
//    public void onClick(View v) {
//        phone_num = input_phone.getText().toString().trim();
//
//        verification = input_verification.getText().toString();
//        password = "123123";  //暂时写死
//        //假类型，暂时测试
//        user_type = "1";
//        switch (v.getId()) {
//
//            //验证码一键登录
//            case R.id.btn_login_sign:
//                //传入其他参数
//                MyUser user = new MyUser();
//                user.setMobilePhoneNumber(phone_num);//设置手机号码（必填）
//                user.setIsCreatedGroup("1");
//                user.signOrLogin(mycontext, verification, new SaveListener() {
//
//                    @Override
//                    public void onSuccess() {
//                        Log.i("smile", "用户登陆成功");
//                        mycontext.toast("用户登录成功");
//                        mycontext.startActivity(new Intent(mycontext, MainActivity.class));
//
//                        bmobObjectId = BmobUser.getCurrentUser(mycontext, MyUser.class).getObjectId();
//                        groupName = bmobObjectId + "房间";
//                        desc = "八字先生聊天室: " + bmobObjectId;
//                        //环信注册
//                        new Thread(new Runnable() {
//                            public void run() {
//                                // 调用sdk注册方法
//                                try {
//                                    EMChatManager.getInstance().createAccountOnServer(bmobObjectId, password);
//                                    chatLogin(); //直接登录
//                                } catch (EaseMobException e1) {
//                                    e1.printStackTrace();
//                                    int errorCode = e1.getErrorCode();
//                                    if (errorCode == EMError.NONETWORK_ERROR) {
//                                        Toast.makeText(mycontext, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
//                                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
////                                                    Toast.makeText(mycontext, "用户已存在！", Toast.LENGTH_SHORT).show();
//                                        chatLogin(); //直接登录
//                                    } else if (errorCode == EMError.UNAUTHORIZED) {
//                                        Toast.makeText(mycontext, "注册失败，无权限！", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(mycontext, "注册失败: " + e1.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }).start();
//                        mycontext.finish();
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        // TODO Auto-generated method stub
//                        mycontext.toast("错误码：" + code + ",错误原因：" + msg);
//                    }
//
//
//                });
//
//
////                BmobUser.signOrLoginByMobilePhone(mycontext, phone_num, verification, new LogInListener<MyUser>() {
////
////                            @Override
////                            public void done(MyUser user, final BmobException e) {
////                                // TODO Auto-generated method stub
////                                if (user != null) {
////                                    Log.i("smile", "用户登陆成功");
////                                    mycontext.toast("用户登录成功");
////                                    mycontext.startActivity(new Intent(mycontext, MainActivity.class));
////                                    mycontext.finish();
////                                    groupName = BmobUser.getCurrentUser(mycontext, MyUser.class).getUsername() + "房间";
////                                    desc = "八字先生聊天室: " + BmobUser.getCurrentUser(mycontext, MyUser.class).getUsername();
////                                    //环信注册
////                                    new Thread(new Runnable() {
////                                        public void run() {
////                                            // 调用sdk注册方法
////                                            try {
////                                                EMChatManager.getInstance().createAccountOnServer(phone_num, password);
////                                                chatLogin(); //直接登录
////                                            } catch (EaseMobException e1) {
////                                                e1.printStackTrace();
////                                                int errorCode = e1.getErrorCode();
////                                                if (errorCode == EMError.NONETWORK_ERROR) {
////                                                    Toast.makeText(mycontext, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
////                                                } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
//////                                                    Toast.makeText(mycontext, "用户已存在！", Toast.LENGTH_SHORT).show();
////                                                    chatLogin(); //直接登录
////                                                } else if (errorCode == EMError.UNAUTHORIZED) {
////                                                    Toast.makeText(mycontext, "注册失败，无权限！", Toast.LENGTH_SHORT).show();
////                                                } else {
////                                                    Toast.makeText(mycontext, "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                                }
////                                            }
////                                        }
////                                    }).start();
////
////                                } else {
////                                    mycontext.toast("登录失败");
////                                }
////                            }
////                        }
////                );
//                break;
//
//
//            //账户名及密码登录
//            case R.id.btn_login:
//                BmobUser bu2 = new BmobUser();
//                bu2.setUsername(phone_num);
//                bu2.setPassword(password);
//                bu2.login(mycontext, new
//
//                                SaveListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("登录成功");
//                                        mycontext.startActivity(new Intent(mycontext, MainActivity.class));
//                                        //用户信息共享给全局
//                                        MyUser myUser = new MyUser();
//                                        myUser.setType(user_type);
//                                        myUser.setUsername(phone_num);
//                                        MyApplication.getInstance().setMyUser(myUser);
//                                        chatLogin();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int code, String msg) {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("登录失败:" + msg);
//                                    }
//                                }
//
//                );
//                break;
//            //获得验证码
//            case R.id.btn_get_verification:
//
//                if (phone_num == null || phone_num.length() == 0) {
//                    mycontext.toast("请输入手机号");
//                } else
//
//                {
//
//                    //添加倒计时
//                    new CountDownTimer(60000, 1000) {
//                        @Override
//                        public void onTick(long millisUntilFinished) {
//                            int s = (int) (millisUntilFinished / 1000);
//                            btn_getverification.setText(s + "s获取");
//                            btn_getverification.setEnabled(false);
//                            ;
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            btn_getverification.setEnabled(true);
//                            ;
//                            btn_getverification.setText("验证码");
//                        }
//                    }.start();
//                    //手机号码一键登录
//                    BmobSMS.requestSMSCode(mycontext, phone_num, "register", new RequestSMSCodeListener() {
//
//                        @Override
//                        public void done(Integer integer, BmobException e) {
//                            if (e == null) {//验证码发送成功
//                                Log.i("smile", "短信id：" + integer);//用于查询本次短信发送详情
//                            }
//                        }
//                    });
//                }
//
//
//                break;
//            //命主登录
//            case R.id.rb_guest:
//
//                if (verification.length() == 0)
//
//                {
//                    mycontext.toast("请输入验证码");
//                }
//
//                //绑定其他字段
//                user = new MyUser();
//                user.setMobilePhoneNumber(phone_num);//设置手机号码（必填）
//                user.setUsername(phone_num);                  //设置用户名，如果没有传用户名，则默认为手机号码
//                user.setPassword("111111");                  //设置用户密码
//                user.setType("1"); //1是命主 游客
//                //短信验证
//                user.signOrLogin(mycontext, verification, new
//
//                                SaveListener() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("注册或登录成功");
//                                        mycontext.startActivity(new Intent(mycontext, MainActivity.class));
//                                        mycontext.finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int code, String msg) {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("错误码：" + code + ",错误原因：" + msg);
//                                    }
//                                }
//
//                );
//
//
//                break;
//            //大师登录
//            case R.id.rb_augur:
//
//                if (verification.length() == 0)
//
//                {
//                    mycontext.toast("请输入验证码");
//                }
//
//                //绑定其他字段
//                final MyUser user_augur = new MyUser();
//                user_augur.setMobilePhoneNumber(phone_num);//设置手机号码（必填）
//                user_augur.setUsername(phone_num);                  //设置用户名，如果没有传用户名，则默认为手机号码
//                user_augur.setPassword("111111");                  //设置用户密码
//                user_augur.setType("2"); //2是大师 算命先生
//                user_augur.signOrLogin(mycontext, verification, new
//
//                                SaveListener() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("注册或登录成功");
//                                        Log.i("datas", "" + user_augur.getUsername() + "-" + user_augur.getType() + "-" + user_augur.getObjectId());
//                                        mycontext.startActivity(new Intent(mycontext, MainActivity.class));
//                                        mycontext.finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int code, String msg) {
//                                        // TODO Auto-generated method stub
//                                        mycontext.toast("错误码：" + code + ",错误原因：" + msg);
//                                    }
//                                }
//
//                );
//
//
//                break;
//        }
//
//    }
//
//    //环信方法抽取*************登录******************
//    private void chatLogin() {
////        EMChatManager.getInstance().logout();//此方法为同步方法  避免一个账号重复登录，不允许登录
//        //环信登录
//        EMChatManager.getInstance().login(bmobObjectId, password, new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                mycontext.runOnUiThread(new Runnable() {
//                    public void run() {
//                        EMGroupManager.getInstance().loadAllGroups();
//                        EMChatManager.getInstance().loadAllConversations();
//                        Log.d("main", "登陆聊天服务器成功！");
//                        mycontext.toast("环信登录成功");
//                        //大师身份登录创建聊天室
//                        if (BmobUser.getCurrentUser(mycontext, MyUser.class).getType().equals("2")) {
//                            //groupName：要创建的群聊的名称
//                            //desc：群聊简介
//                            //members：群聊成员,为空时这个创建的群组只包含自己
//                            //needApprovalRequired:如果创建的公开群用需要户自由加入，就传false。否则需要申请，等群主批准后才能加入，传true
//                            //2000为最大的群聊人数
//                            //不重复创建房间
//                            if (BmobUser.getCurrentUser(mycontext, MyUser.class).getIsCreatedGroup() == null) {
//                                //更新信息
//                                MyUser myUser = new MyUser();
//                                myUser.setIsCreatedGroup("1");
//                                myUser.update(mycontext, BmobUser.getCurrentUser(mycontext, MyUser.class).getObjectId(), new UpdateListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onFailure(int i, String s) {
//                                    }
//                                });
////                                BmobUser.getCurrentUser(mycontext, MyUser.class).setIsCreatedGroup("1");
//                            }
//                            Log.e("data", BmobUser.getCurrentUser(mycontext, MyUser.class).getIsCreatedGroup() + "isCreatedGroup");
//                            if (BmobUser.getCurrentUser(mycontext, MyUser.class).getIsCreatedGroup().equals("1") || BmobUser.getCurrentUser(mycontext, MyUser.class).getIsCreatedGroup() == null) {
//                                try {
//                                    Log.e("data", "创建群组");
//                                    EMGroupManager.getInstance().createPublicGroup(groupName, desc, null, false, 2000);//需异步处理
//                                    mycontext.toast(BmobUser.getCurrentUser(mycontext, MyUser.class).getObjectId() + "大师，已为您创建聊天室");
//                                    EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());  //监听
//                                    //创建好房间将标记位设置为2
//                                    MyUser myUser = new MyUser();
//                                    myUser.setIsCreatedGroup("2");
//                                    myUser.update(mycontext, BmobUser.getCurrentUser(mycontext, MyUser.class).getObjectId(), new UpdateListener() {
//                                        @Override
//                                        public void onSuccess() {
//                                            Log.e("data", "登录时修改isCreadtedGroup为2");
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(int i, String s) {
//
//                                        }
//                                    });
//
//                                } catch (EaseMobException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.d("main", "登陆聊天服务器失败！");
//            }
//        });
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
//}
