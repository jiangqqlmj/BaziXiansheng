package com.example.administrator.bazipaipan.me.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.adapter.ReceiveSiteSelectListAdapter;
import com.example.administrator.bazipaipan.db.AddressDAO;
import com.example.administrator.bazipaipan.entity.address.CityByProModel;
import com.example.administrator.bazipaipan.entity.address.ProvinceModel;
import com.example.administrator.bazipaipan.entity.address.ZoneByCityModel;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.utils.changehead.Tools;
import com.example.administrator.bazipaipan.utils.cityutils.CityPicker;
import com.example.administrator.bazipaipan.widget.DateTimePickDialogUtil;
import com.example.administrator.bazipaipan.widget.WheelView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.administrator.bazipaipan.R.id.btn_edit_updateinfo;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class EditInfoFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "EditInfoFragment";
    private MeContainerActivity mycontext;
    //出生日期
    private String initEndDateTime = "2000年1月1日 0:00"; // 初始化时间
    //城市选择器
    TextView tv_birthday, tv_city;
    private View areaPopView;
    private PopupWindow areaPopWindow;
    private CityPicker cityPicker;
    private String areaCode;
    //点击更换头像
    ImageView iv_head;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    File tempFile;
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    //取消  应该是系统返回的值
    private static final int RESULT_CANCELED = 0;
    //保存修改
    Button btn_update;
    String username, sex, birthday, city, sign;
    //性别
    TextView tv_edit_sex;
    private static final String[] SEXS = new String[]{"男", "女"};
    //用户名
    private EditText et_username, et_sign;

    //=====城市选择器======
    // 地址选择窗口
    RelativeLayout add_layout_back;
    ListView lv_addr;
    private AlertDialog addrDialog;
    private ReceiveSiteSelectListAdapter listAdapter;
    private List<Object> addrList;
    private AddressDAO addressDAO;
    private int addrIndex = 1;
    private String provinceStr, cityStr, districtStr;// 地址名称
    private String proID, cityID, districtID;// 地址ID
    private int proIndex, cityIndex;

    public EditInfoFragment() {

    }

    public static EditInfoFragment newInstance() {
        EditInfoFragment fragment = new EditInfoFragment();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mycontext = (MeContainerActivity) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        initPupupWindow();
        initDatas();
    }

    //为控件赋初值
    private void initDatas() {
        //初始化城市数据
        addressDAO = new AddressDAO(mycontext);
        //BmobUser中的特定属性
        int gold_num;
        String username = (String) BmobUser.getObjectByKey(mycontext, "username");
        //MyUser中的扩展属性
        String user_type = (String) BmobUser.getObjectByKey(mycontext, "type");
        String sex = (String) BmobUser.getObjectByKey(mycontext, "sex");
        String city = (String) BmobUser.getObjectByKey(mycontext, "city");
        String sign = (String) BmobUser.getObjectByKey(mycontext, "sign");
        String birthday = (String) BmobUser.getObjectByKey(mycontext, "birthday");
//        BmobFile avatar = (BmobFile) BmobUser.getObjectByKey(mycontext, "avatar");

        if (BmobUser.getObjectByKey(mycontext, "goldNum") != null) {
            gold_num = (int) BmobUser.getObjectByKey(mycontext, "goldNum");
        } else {
            gold_num = 0;
        }
        if (TextUtils.isEmpty(username)) {
            et_username.setText("");  //电话号码
        } else {
            et_username.setText(username);  //电话号码
        }
        //根据用户类别判断：
//        if (avatar != null) {
//
//        }
        if (TextUtils.isEmpty(sex + "")) {
            tv_edit_sex.setText("男");
        } else {
            tv_edit_sex.setText(sex + "");
        }
        if (TextUtils.isEmpty(birthday + "")) {
            tv_birthday.setText(initEndDateTime);
        } else {
            tv_birthday.setText(birthday + "");
        }
        if (TextUtils.isEmpty(city + "")) {
            tv_city.setText("北京");
        } else {
            tv_city.setText(city + "");
        }
        if (TextUtils.isEmpty(sign + "")) {
            et_sign.setText("");
        } else {
            et_sign.setText(city + "");
        }

    }


    private void initPupupWindow() {
        areaPopView = mycontext.getLayoutInflater().inflate(R.layout.pop_layout, null);
        areaPopWindow = new PopupWindow(areaPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        areaPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        areaPopWindow.setOutsideTouchable(true);
    }

    private void initviews() {
        //
        et_sign = (EditText) mycontext.findViewById(R.id.et_input_sign);

        et_username = (EditText) mycontext.findViewById(R.id.et_edit_username);
        //
        tv_edit_sex = (TextView) mycontext.findViewById(R.id.tv_edit_sex);
        tv_edit_sex.setOnClickListener(this);
        btn_update = (Button) mycontext.findViewById(btn_edit_updateinfo);
        btn_update.setOnClickListener(this);
        tv_birthday = (TextView) mycontext.findViewById(R.id.tv_editdata_birthday);
        tv_birthday.setOnClickListener(this);
        tv_city = (TextView) mycontext.findViewById(R.id.tv_editdata_city);
        tv_city.setOnClickListener(this);
        cityPicker = (CityPicker) mycontext.findViewById(R.id.citypicker);
        iv_head = (ImageView) mycontext.findViewById(R.id.iv_edit_change_myhead);
        //非网络头像处理
        String user_type = (String) BmobUser.getObjectByKey(mycontext, "type");
        if (user_type.equals("1")) {
            iv_head.setImageResource(R.drawable.guest_head_me);
        } else {
            iv_head.setImageResource(R.drawable.augur_head_me);
        }
        //从网络中获取头像进行替换
        iv_head.setOnClickListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_edit_data, null);
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
    public void onClick(View v) {
        switch (v.getId()) {
            //sex
            case R.id.tv_edit_sex:
                View outerView = LayoutInflater.from(mycontext).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(1);
                wv.setItems(Arrays.asList(SEXS));
                wv.setSeletion(2);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.e("datas", "selectedIndex" + selectedIndex + "   item" + item);
                        if (selectedIndex == 1) {
                            tv_edit_sex.setText("男");
                        } else if (selectedIndex == 2) {
                            tv_edit_sex.setText("女");
                        } else {
                            tv_edit_sex.setText("女");
                        }
                    }

                });

                new AlertDialog.Builder(mycontext)
                        .setTitle("选择性别")
                        .setView(outerView)
                        .setPositiveButton("OK", null)
                        .show();

                break;
            //出生日期
            case R.id.tv_editdata_birthday:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        mycontext, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(tv_birthday);
                break;
            //选择城市
            case R.id.tv_editdata_city:
                //showPopWindow(v);
                //修改城市弹出框
                showAddressSelect();
                break;
            //换头像
            case R.id.iv_edit_change_myhead:
                showDialog();
                break;

            //保存修改（更新user数据） 只能根据object id修改
            case R.id.btn_edit_updateinfo:
                final MyUser newuser = new MyUser();
                username = et_username.getText().toString();
                sex = tv_edit_sex.getText().toString();
                birthday = tv_birthday.getText().toString();
                city = tv_city.getText().toString();
                sign = et_sign.getText().toString();
                final BmobUser bmobuser = BmobUser.getCurrentUser(mycontext,
                        MyUser.class);
                newuser.setUsername(username);
                newuser.setSex(sex);
                newuser.setBirthday(birthday);
                newuser.setCity(city);
                newuser.setSign(sign);
                //更新
                newuser.update(mycontext, bmobuser.getObjectId(), new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.i("bmob", "更新成功：");
                        mycontext.toast("修改成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Log.i("bmob", "更新失败：" + msg);
                        mycontext.toast("修改失败");
                    }
                });
                break;
            case R.id.layout_receive_site_add_close: // 关闭所在地区弹出框
                addrDialog.dismiss();
                break;
            case R.id.tv_receive_site_back:
                if (addrIndex == 3) {
                    addrIndex = 2;
                    addrList = addressDAO.getAllCityByProvince(proID);
                    listAdapter.setNewData(2, addrList);
                    listAdapter.notifyDataSetChanged();
                    lv_addr.setSelection(cityIndex);
                } else if (addrIndex == 2) {
                    add_layout_back.setVisibility(View.GONE);
                    addrIndex = 1;
                    addrList = (List<Object>) addressDAO.getAllProvince();
                    listAdapter.setNewData(1, addrList);
                    listAdapter.notifyDataSetChanged();
                    lv_addr.setSelection(proIndex);
                }
                break;
        }
    }

    //以下是换头像方法

    /**
     * 显示换头像的dialog
     */
    private void showDialog() {

        new AlertDialog.Builder(mycontext)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Tools.hasSdcard()) {

                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }
                                //虚拟机上拍照会崩溃
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
//            Bitmap mBitmap = (Bitmap) data.getExtras().get("data");
//            mBitmap = BitmapUtil.getBitmap(mBitmap, 50);
//            File localimg = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "srcs");
//            if (!localimg.exists()) {
//                localimg.mkdirs();
//            }
//            File iconfile = new File(localimg.getAbsolutePath() + "/usericon.jpg");
//            if (iconfile.exists()) {
//                iconfile.delete();
//            }
//            OutputStream out = null;
//            try {
//                out = new FileOutputStream(iconfile);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            //2bitmap压缩
//            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            final MyUser myUser = new MyUser();
//            new BmobFile(iconfile).upload(mycontext,
//                    new UploadFileListener() {
//
//                        @Override
//                        public void onSuccess() {
//                            // TODO Auto-generated method stub
//                            myUser.update(mycontext,
//                                    BmobUser.getCurrentUser(mycontext)
//                                            .getObjectId(),
//                                    new UpdateListener() {
//
//                                        @Override
//                                        public void onSuccess() {
//                                            // TODO Auto-generated method
//                                            mycontext.toast("更新头像成功");
////                                            iv_head.setImageBitmap(bitmap1);
//                                        }
//
//                                        @Override
//                                        public void onFailure(int arg0,
//                                                              String arg1) {
//                                            // TODO Auto-generated method
//                                            mycontext.toast("头像更新失败");
//                                        }
//                                    });
//
//                        }
//
//                        @Override
//                        public void onFailure(int arg0, String arg1) {
//                            // TODO Auto-generated method stub
//                            mycontext.toast("UploadFile 失败");
//                        }
//                    });

            //判断条件
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        //文件
                        tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + IMAGE_FILE_NAME);
                        //临时文件
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(mycontext, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        //把文件压缩保存到本地sdcard
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            //faceImage是头像
            iv_head.setImageDrawable(drawable);
            //保存到服务器

        }
    }

    //    以上是修改头像方法
    private void showPopWindow(View view) {
        if (areaPopWindow == null) {
            initPupupWindow();
        }
        cityPicker = (CityPicker) areaPopView.findViewById(R.id.citypicker);
        areaPopView.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaCode = cityPicker.getCity_code_string();
                tv_city.setText(cityPicker.getCity_string());

                Log.i(TAG, "地区码：" + areaCode + "，地区名：" + cityPicker.getCity_string());
                dismissPopWindow();
            }
        });
        areaPopView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
            }
        });

        areaPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void dismissPopWindow() {
        if (areaPopWindow != null && areaPopWindow.isShowing()) {
            areaPopWindow.dismiss();
        }
    }


    //============地区弹出框=================
    private void showAddressSelect(){
        if (addrDialog == null) {
            addrDialog = new AlertDialog.Builder(mycontext).create();
            View contentView = LayoutInflater.from(
                    mycontext).inflate(
                    R.layout.receive_site_add_addrselect_dialog_layout,
                    null);
            lv_addr = (ListView) contentView
                    .findViewById(R.id.lv_receive_site_add);
            RelativeLayout img_close = (RelativeLayout) contentView
                    .findViewById(R.id.layout_receive_site_add_close);
            add_layout_back = (RelativeLayout) contentView
                    .findViewById(R.id.layout_receive_site_back);
            TextView bt_back = (TextView) contentView
                    .findViewById(R.id.tv_receive_site_back);
            addrList = (List<Object>) addressDAO.getAllProvince();
            listAdapter = new ReceiveSiteSelectListAdapter(
                    getActivity(), addrList, 1);
            lv_addr.setAdapter(listAdapter);
            lv_addr.setOnItemClickListener(new MyOnItemClickListener());
            img_close.setOnClickListener(this);
            bt_back.setOnClickListener(this);
            addrDialog.show();
            Window window = addrDialog.getWindow();
            window.setWindowAnimations(R.style.animation_receive_site_window);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = dip2px(mycontext, 450);
            window.setAttributes(lp);
            addrDialog.setContentView(contentView);
            addrDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        } else {
            if (addrDialog.isShowing()) {
                addrDialog.dismiss();
            } else {
                addrDialog.show();
            }
        }
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            lv_addr.setSelection(0);
            if (addrIndex == 1) {
                addrIndex = 2;
                add_layout_back.setVisibility(View.VISIBLE);
                proIndex = (int) id;
                ProvinceModel model = (ProvinceModel) addrList.get(proIndex);
                provinceStr = model.getProName();
                proID = model.getProSort();
                addrList = addressDAO.getAllCityByProvince(proID);
                listAdapter.setNewData(2, addrList);
                listAdapter.notifyDataSetChanged();
                lv_addr.setSelection(0);
            } else if (addrIndex == 2) {
                addrIndex = 3;
                cityIndex = (int) id;
                CityByProModel model = (CityByProModel) addrList.get(cityIndex);
                cityStr = model.getCityName();
                cityID = model.getCitySort();
                addrList = addressDAO.getAllDistrictByCity(cityID);
                if (addrList.size() == 0) {
                    districtStr = "";
                    districtID = cityID;
                    addrDialog.dismiss();
                    tv_city.setText(provinceStr + "," + cityStr);
                    proIndex = 0;
                    cityIndex = 0;
                    addrIndex = 1;
                    addrList = addressDAO.getAllProvince();
                    listAdapter.setNewData(1, addrList);
                } else {
                    listAdapter.setNewData(3, addrList);
                }
                listAdapter.notifyDataSetChanged();
                lv_addr.setSelection(0);
            } else {
                addrIndex = 1;
                ZoneByCityModel model = (ZoneByCityModel) addrList
                        .get((int) id);
                districtStr = model.getZoneName();
                districtID = model.getZoneID();
                addrDialog.dismiss();
                tv_city.setText(provinceStr + "," + cityStr + ","
                        + districtStr);
                add_layout_back.setVisibility(View.GONE);
                proIndex = 0;
                cityIndex = 0;
                addrList = addressDAO.getAllProvince();
                listAdapter.setNewData(1, addrList);
                listAdapter.notifyDataSetChanged();
                lv_addr.setSelection(0);
            }
        }
    }
}
