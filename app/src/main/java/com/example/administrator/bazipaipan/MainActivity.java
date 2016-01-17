package com.example.administrator.bazipaipan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseFragment;
import com.example.administrator.bazipaipan.augur.fragment.AugurFragment;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;
import com.example.administrator.bazipaipan.homepage.view.fragment.fragment.HomePageFragment;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.fragment.MeFragment;
import com.example.administrator.bazipaipan.me.view.fragment.RechargeFragment;
import com.example.administrator.bazipaipan.search.SearchContainerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private String content[];
    private int img[];
    private LinearLayout layout;
    private ListView mListView;
    private PopupWindow pw;
    //-----------------------------
    private static final String TAG = "MainActivity";
    private MainActivity mycontext;
    private MyPagerFragmentAdapter myadapter;
    private ViewPager vp;
    private List<Fragment> list_frags;
    Fragment home_fragment, augur_fragment, amuse_fragment, me_fragment;
    //未选中和选中图片
    private int[] imgnor = {R.drawable.nor_homepage,
            R.drawable.nor_augur, R.drawable.nor_amuse,
            R.drawable.nor_me};
    private int[] imgsel = {R.drawable.sel_homepage,
            R.drawable.sel_augur,
            R.drawable.sel_amuse,
            R.drawable.sel_me};
    //viewpager中记载的图片
    private int[] imgcont = {R.drawable.show_homepage, R.drawable.show_augur, R.drawable.show_amuse, R.drawable.show_me};
    private LinearLayout[] tabs = new LinearLayout[4];
    //actionbar相关
    ActionBar ab;
    private String[] titles = {"八字排盘", "八字先生", "江湖", "个人中心"};
    private TextView toolbar_title_main;
    //搜索图片切换
    private boolean showmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymain);
        mycontext = this;
        showmenu = true;
        Bmob.initialize(this, "f93db7bdfd1f0c1657b956588038115f");
        EMChat.getInstance().setAppInited();  //环信 UI初始化完毕  注册监听文档
        initViews();
        ButterKnife.inject(this);
        initDatas();
        myadapter = new MyPagerFragmentAdapter(getSupportFragmentManager(), list_frags, MainActivity.this);
        vp.setAdapter(myadapter);
        vp.setOnPageChangeListener(this);
        //笑傲江湖跳转
        if (MainActivity.this.getIntent().getStringExtra(PAGETO) != null) {
            changeItem(2);
            vp.setCurrentItem(2);
            toolbar_title_main.setText("江湖");
            icon_menu.setImageResource(R.drawable.menu_search);
            showmenu = false;
            list_frags.clear();
            initDatas();
            myadapter.setDatas(list_frags);
        }
    }

    //首页菜单
    @InjectView(R.id.icon_menu)
    ImageView icon_menu;

    @OnClick(R.id.icon_menu)
    public void showPopwindow() {
        if (showmenu) {

            content = new String[]{"充值金币", "个人中心", "开始测算"};
            img = new int[]{R.drawable.item_recharge, R.drawable.item_personal, R.drawable.item_divinate};
            layout = (LinearLayout) getLayoutInflater().inflate(R.layout.action_popuwindow, null);
            mListView = (ListView) layout.findViewById(R.id.lv_popu);
            List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < 3; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("tupian", img[i]);
                map.put("neirong", content[i]);
                mList.add(map);
            }
            Log.i("Log", mList.size() + "");
            mListView.setAdapter(new SimpleAdapter(MainActivity.this, mList, R.layout.item_popu, new String[]{"tupian", "neirong"}, new int[]{R.id.show_img, R.id.show_con}));

            pw = new PopupWindow(layout, 316, 316);
            pw.setFocusable(true);
            ColorDrawable cd = new ColorDrawable(Color.parseColor("#00b58043"));
            pw.setBackgroundDrawable(cd);
            pw.showAsDropDown(icon_menu, 280, 80);
            //点击跳转
            MyClick();
        } else {
            //跳转搜索
            this.startActivity(new Intent(this, SearchContainerActivity.class));
        }
    }


    public void MyClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pw.isShowing()) {
                    pw.dismiss();
                }
                Intent intent = new Intent();
                intent.setClass(mycontext, MeContainerActivity.class);
                if (position == 0) {
                    intent.putExtra(BaseActivity.PAGETO, RechargeFragment.TAG);
                    startActivity(intent);
                } else if (position == 1) {
                    changeItem(3);
                    vp.setCurrentItem(3);
                    toolbar_title_main.setText("个人中心");
                    betterpager();
                } else if (position == 2) {
                    Intent intent1 = new Intent(mycontext, ChatContainerActivity.class);
                    //传参

                    startActivity(intent1);
                }
            }
        });
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        list_frags = new ArrayList<Fragment>();

        home_fragment = new HomePageFragment();
        augur_fragment = new AugurFragment();
        amuse_fragment = new AmuseFragment();
        me_fragment = new MeFragment();

        list_frags.add(home_fragment);
        list_frags.add(augur_fragment);
        list_frags.add(amuse_fragment);
        list_frags.add(me_fragment);

    }

    private void initViews() {
        // TODO Auto-generated method stub
        toolbar_title_main = (TextView) findViewById(R.id.toolbar_title_main);
        vp = (ViewPager) findViewById(R.id.main_vp);
        tabs[0] = (LinearLayout) findViewById(R.id.main_tab01);
        tabs[1] = (LinearLayout) findViewById(R.id.main_tab02);
        tabs[2] = (LinearLayout) findViewById(R.id.main_tab03);
        tabs[3] = (LinearLayout) findViewById(R.id.main_tab04);
        for (int i = 0; i < 4; i++) {
            tabs[i].setOnClickListener(this);
        }
//        actionbar的切换
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
//            ab.setHomeAsUpIndicator(R.drawable.back);
//            给左上角图标的左边加上一个返回的图标
//            ab.setDisplayHomeAsUpEnabled(true);
            //隐藏icon logo
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(true);
//            ab.setTitle("八字先生");
            toolbar_title_main.setText("八字先生");
        }

    }

    //tabview的点击效果 切换逻辑
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.main_tab01:
                changeItem(0);
                vp.setCurrentItem(0);
                toolbar_title_main.setText("八字排盘");
                icon_menu.setImageResource(R.drawable.icon_menu);
                showmenu = true;
                betterpager();
                break;
            case R.id.main_tab02:
                changeItem(1);
                vp.setCurrentItem(1);
                toolbar_title_main.setText("八字先生");
                icon_menu.setImageResource(R.drawable.menu_search);
                showmenu = false;
                betterpager();
                break;
            case R.id.main_tab03:
                changeItem(2);
                vp.setCurrentItem(2);
                toolbar_title_main.setText("江湖");
                icon_menu.setImageResource(R.drawable.menu_search);
                showmenu = false;
                list_frags.clear();
                initDatas();
                myadapter.setDatas(list_frags);
                break;
            case R.id.main_tab04:
                changeItem(3);
                vp.setCurrentItem(3);
                toolbar_title_main.setText("个人中心");
                icon_menu.setImageResource(R.drawable.icon_menu);
                showmenu = true;
                list_frags.clear();
                initDatas();
                myadapter.setDatas(list_frags);
                break;

            default:
                break;
        }
    }

    /**
     * tabview底部导航栏的状态变化  颜色  文字
     */
    public void changeItem(int pos) {
        for (int i = 0; i < 4; i++) {
            if (pos == i) {
                ((ImageView) tabs[i].getChildAt(0)).setImageResource(imgsel[i]);
                ((TextView) tabs[i].getChildAt(1)).setTextColor(Color.parseColor("#000000"));
            } else {
                ((ImageView) tabs[i].getChildAt(0)).setImageResource(imgnor[i]);
                ((TextView) tabs[i].getChildAt(1)).setTextColor(Color.parseColor("#999999"));
            }
        }
    }

    /**
     * viewpager滑动效果相关
     */

    //
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        changeItem(arg0);
        //viewpager滑动切换标题栏
        toolbar_title_main.setText(titles[arg0]);
        if (arg0 == 1 || arg0 == 2) {
            icon_menu.setImageResource(R.drawable.menu_search);
            showmenu = false;
        } else {
            icon_menu.setImageResource(R.drawable.icon_menu);
            showmenu = true;
        }
        betterpager();
    }

    //fragmentpageradapter优化
    public void betterpager() {
        list_frags.clear();
        initDatas();
        myadapter.setDatas(list_frags);
    }

}
