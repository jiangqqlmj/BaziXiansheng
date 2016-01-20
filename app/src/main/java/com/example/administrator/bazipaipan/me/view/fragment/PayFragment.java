package com.example.administrator.bazipaipan.me.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.adapter.RechargeAdapter;
import com.example.administrator.bazipaipan.me.view.alipay.PayResult;
import com.example.administrator.bazipaipan.me.view.alipay.SignUtils;
import com.example.administrator.bazipaipan.me.view.wechatpay.Constants;
import com.example.administrator.bazipaipan.me.view.wechatpay.MD5;
import com.example.administrator.bazipaipan.me.view.wechatpay.Util;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class PayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RechargeAdapter.IClickListener {
    public static final String TAG = "PayFragment";
    private MeContainerActivity mycontext;

    @InjectView(R.id.tv_currentpayuser)
    TextView tv_currentpayuser;
    @InjectView(R.id.tv_currentpaygoods)
    TextView tv_currentpaygoods;

    @InjectView(R.id.tv_needpaymoney)
    TextView tv_needpaymoney;
    @InjectView(R.id.paycontainer_weixin)
    RelativeLayout paycontainer_weixin;
    @InjectView(R.id.paycontainer_zhifubao)
    RelativeLayout paycontainer_zhifubao;
    @InjectView(R.id.paynext_btn)
    Button paynext_btn;
    @InjectView(R.id.weixin_gou_iv)
    ImageView weixin_gou_iv;
    @InjectView(R.id.zhifubao_gou_iv)
    ImageView zhifubao_gou_iv;

    private int coinNum;
    private float paymoney;
    private String username;
    private boolean weichatpay;
    //支付宝支付
    // 商户PID
    public static final String PARTNER = "";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        updateuserGold();
                        Toast.makeText(mycontext, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mycontext, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mycontext, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mycontext, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    //付款成功，更新用户金币数量
    private void updateuserGold() {
        final String uid = BmobUtils.getCurrentId(mycontext);
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(mycontext, uid, new GetListener<MyUser>() {

            @Override
            public void onSuccess(MyUser object) {
                // TODO Auto-generated method stub
                int nowgoldNum = object.getGoldNum();
                //更新数据
                MyUser myUser = new MyUser();
                final int curGoldNum = coinNum + nowgoldNum;
                myUser.setGoldNum(curGoldNum);
                myUser.update(mycontext, uid, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        mycontext.toast("充值成功");
                        BmobUtils.log("当前金币数量" + curGoldNum);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        BmobUtils.log("金币支付失败" + s);

                    }
                });
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                BmobUtils.log("金币查询失败" + arg0);
            }
        });


    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void alipaymain() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mycontext)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //关闭支付窗口
                                    mycontext.finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(coinNum + "金币", "八字先生金币", paymoney + "");


        // 对订单做RSA 签名
        String sign = sign(orderInfo);

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mycontext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mycontext);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mycontext);
        String version = payTask.getVersion();
        Toast.makeText(mycontext, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
    //------------以上是ali支付宝支付--------------

    //---------微信支付---------------
//    private static final String TAG = "MicroMsg.SDKSample.PayActivity";

    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(mycontext, null);
    TextView show;
    Map<String, String> resultunifiedorder;
    StringBuffer sb;

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", "----" + packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion", "----" + appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", "----" + sb.toString());
        return sb.toString();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mycontext, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            show.setText(sb.toString());

            resultunifiedorder = result;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion", "----" + entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion", "----" + content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }

    }


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", "----" + e.toString());
        }
        return null;

    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genOutTradNo() {
        Random random = new Random();
//		return "COATBAE810"; //订单号写死的话只能支付一次，第二次不能生成订单
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    //
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "APP pay test"));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "2000000"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));


            String xmlstring = toXml(packageParams);

            return xmlstring;

        } catch (Exception e) {
            Log.e(TAG, "----genProductArgs fail, ex = " + e.getMessage());
            return null;
        }


    }

    //获得签名参数
    private void genPayReq() {
        //生成prepay_id
        getprepay_id();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        show.setText(sb.toString());

        Log.e("orion", "----" + signParams.toString());

    }

    private void sendPayReq() {

        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    //---------以上是微信支付------------

    //单例思想获得实例对象
    public PayFragment() {
    }

    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MeContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        coinNum = getActivity().getIntent().getIntExtra(RechargeFragment.PAYGOODS, 0);
        paymoney = getActivity().getIntent().getFloatExtra(RechargeFragment.PAYMONEY, 0);
        username = BmobUtils.getCurrentUser(mycontext).getUsername();
        if (!TextUtils.isEmpty(coinNum + "")) {
            tv_currentpaygoods.setText(coinNum + "");
        }
        if (!TextUtils.isEmpty(paymoney + "")) {
            tv_needpaymoney.setText(paymoney + "");
        }
        if (username != null && !TextUtils.isEmpty(username)) {
            tv_currentpayuser.setText(username);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pay, container, false);
        ButterKnife.inject(this, view);
        updateUI(view);
        wechatOncreate();
        return view;
    }

    //生成prepay_id
    public void getprepay_id() {
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }


    private void wechatOncreate() {
        req = new PayReq();
        sb = new StringBuffer();


//        payBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//                getPrepayId.execute();
//            }
//        });

        //生成签名参数
        genPayReq();
//        appay_pre_btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                genPayReq();
//            }
//        });
        //		调起微信支付
        sendPayReq();
//        appayBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                sendPayReq();
//            }
//        });
    }

    @OnClick(R.id.paycontainer_weixin)
    public void weixinpay() {
        weixin_gou_iv.setImageResource(R.drawable.paygou);
        zhifubao_gou_iv.setImageResource(R.drawable.paynogou);
        weichatpay = true;
    }

    @OnClick(R.id.paycontainer_zhifubao)
    public void alipay() {
        weixin_gou_iv.setImageResource(R.drawable.paynogou);
        zhifubao_gou_iv.setImageResource(R.drawable.paygou);
        weichatpay = false;
    }

    @OnClick(R.id.paynext_btn)
    public void payNext() {
        if (weichatpay) {
            //微信支付
            wechatpaymain();
            mycontext.toast("微信支付");
        } else {
            alipaymain();
        }
        return;

    }

    //微信支付
    private void wechatpaymain() {
        sendPayReq();
    }

    private void updateUI(View view) {
        onRefresh();
    }

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


    @Override
    public void onRefresh() {
        updateFromNet();
    }

    private void updateFromNet() {
    }


    //点击item的跳转逻辑
    @Override
    public void onItemClicked(int position) {


    }
}
