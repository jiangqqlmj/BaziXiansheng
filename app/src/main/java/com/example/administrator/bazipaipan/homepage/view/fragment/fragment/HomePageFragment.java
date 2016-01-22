package com.example.administrator.bazipaipan.homepage.view.fragment.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.MainActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.activity.AmuseContainerActivity;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseCommentFragment;
import com.example.administrator.bazipaipan.amuse.view.activity.fragment.AmuseFragment;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseDetail;
import com.example.administrator.bazipaipan.homepage.HomepageContainerActivity;
import com.example.administrator.bazipaipan.homepage.view.fragment.adapter.NewsAmuseAdapter;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.example.administrator.bazipaipan.utils.NetUtil;
import com.example.administrator.bazipaipan.utils.sharedPreferencesUtils;
import com.example.administrator.bazipaipan.widget.DateTimePickDialogUtil;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.FullyLinearLayoutManager;
import com.example.administrator.bazipaipan.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */

public class HomePageFragment extends Fragment implements NewsAmuseAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TabLayout.OnTabSelectedListener {
    public static final String TAG = "HomePageFragment";
    private MainActivity mycontext;
    //自定义测试
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private MymazhaFragment mymazhaFragment;
    private RecommendLookerFragment recommendLookerFragment;//Fragment视图
    //新闻列表
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<AmuseDetail> datas;
    //4适配器
    private NewsAmuseAdapter mAdapter;
    //5网络加载更多
//    private VerticalSwipeRefreshLayout mSwipeLayout;
    //6日期及性别选择器相关
    TextView tv_birthday;
    private String initEndDateTime = "2000年1月1日 0:00"; // 初始化时间
    LinearLayout con_sex, con_datetype, con_birthday;
    //测算信息弹出框逻辑
    LinearLayout container_popup, container_homepage_birthtime, container_birthday, container_datetype, container_sex;
    EditText input_name;
    TextView input_sex, input_datetype, input_date, tv_homepage_birthtime;
    Button btn_divination_homepage;
    boolean ispop = false;
    //笑傲江湖
    private static final String[] SEXS = new String[]{"男", "女"};
    private static final String[] TYPES = new String[]{"阳历", "阴历"};
    Button btn_divination;
    private RelativeLayout container_toamuse;
    //测算历史
    LinearLayout container_history_recorder;
    int cesuanNum;
    TextView btn_history1, btn_history2, btn_history3, btn_history4, btn_history5;
    //断网提醒
    ImageView btn_nonet;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mycontext = (MainActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initviews();
        //有缓存信息，则显示测算过的人
        hascache();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_homepage, container, false);
        //recyclerview
        ButterKnife.inject(this, view);
        updateRecyclerViewUI(view);
        return view;

    }


    private void updateRecyclerViewUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_amusenews_list);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //适配器未装填内容
        mAdapter = new NewsAmuseAdapter(mycontext, this);
        recyclerView.setAdapter(mAdapter);
        //滑动加载更多的layout
//        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_bright);
//        //需要实现方法     测试时暂时关闭
//        mSwipeLayout.setOnRefreshListener(this);
        //刷新数据
//        onRefresh();
        updateFromNet();
    }

    private void initviews() {

        //断网提醒
        btn_nonet = (ImageView) mycontext.findViewById(R.id.btn_nonet);
        btn_nonet.setOnClickListener(this);
        //测算历史
        container_history_recorder = (LinearLayout) mycontext.findViewById(R.id.container_history_recorder);
        container_history_recorder.setOnClickListener(this);
        btn_history1 = (TextView) mycontext.findViewById(R.id.btn_history1);
        btn_history1.setOnClickListener(this);
        btn_history2 = (TextView) mycontext.findViewById(R.id.btn_history2);
        btn_history2.setOnClickListener(this);
        btn_history3 = (TextView) mycontext.findViewById(R.id.btn_history3);
        btn_history3.setOnClickListener(this);
        btn_history4 = (TextView) mycontext.findViewById(R.id.btn_history4);
        btn_history4.setOnClickListener(this);
        btn_history5 = (TextView) mycontext.findViewById(R.id.btn_history5);
        btn_history5.setOnClickListener(this);
        //笑傲江湖
        container_toamuse = (RelativeLayout) mycontext.findViewById(R.id.container_toamuse);
        container_toamuse.setOnClickListener(this);
        //时辰
        container_homepage_birthtime = (LinearLayout) mycontext.findViewById(R.id.container_homepage_birthtime);
        container_homepage_birthtime.setOnClickListener(this);
        tv_homepage_birthtime = (TextView) mycontext.findViewById(R.id.tv_homepage_birthtime);
        //测算信息弹出
        btn_divination_homepage = (Button) mycontext.findViewById(R.id.btn_divination_homepage);
        btn_divination_homepage.setOnClickListener(this);
        container_popup = (LinearLayout) mycontext.findViewById(R.id.ll_container_popup);
        input_name = (EditText) mycontext.findViewById(R.id.et_input_name);
        input_sex = (TextView) mycontext.findViewById(R.id.tv_homepage_sex);
        input_sex.setOnClickListener(this);
        input_datetype = (TextView) mycontext.findViewById(R.id.tv_homepage_datetype);
        input_datetype.setOnClickListener(this);
        btn_divination = (Button) mycontext.findViewById(R.id.btn_homepage_divinate);
        btn_divination.setOnClickListener(this);
        //测算相关及选择器
        con_sex = (LinearLayout) mycontext.findViewById(R.id.container_homepage_sex);
        con_sex.setOnClickListener(this);
        con_birthday = (LinearLayout) mycontext.findViewById(R.id.container_homepage_birthday);
        con_birthday.setOnClickListener(this);
        con_datetype = (LinearLayout) mycontext.findViewById(R.id.container_homepage_datetype);
        con_datetype.setOnClickListener(this);
        tv_birthday = (TextView) mycontext.findViewById(R.id.tv_homepage_birthday);
        //tabview及viewpager联动效果
        mViewPager = (ViewPager) mycontext.findViewById(R.id.vp_view);
//        mViewPager.setOffscreenPageLimit(1);  不预加载
        mTabLayout = (TabLayout) mycontext.findViewById(R.id.tabs);
        //添加页卡视图
        mFragmentList.add(recommendLookerFragment);
        mFragmentList.add(mymazhaFragment);
        //添加页卡标题
        mTitleList.add("围观推荐");
        mTitleList.add("我的马扎");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getFragmentManager(), mFragmentList, mycontext);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        if (mViewPager != null) {
            setupViewPager(mViewPager);
//            mTabLayout.setupWithViewPager(mViewPager);
//            mTabLayout.setOnTabSelectedListener(this);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(mycontext.getSupportFragmentManager());
        recommendLookerFragment = new RecommendLookerFragment();
        fragmentAdapter.addFragment(recommendLookerFragment, getString(R.string.recommendlooker));
        mymazhaFragment = new MymazhaFragment();
        fragmentAdapter.addFragment(mymazhaFragment, getString(R.string.myfocus));
        viewPager.setAdapter(fragmentAdapter);
    }

    //tab点击切换
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        mViewPager.setCurrentItem(pos);
        switch (pos) {
            case 0:
                recommendLookerFragment.onRefresh();
                break;
            case 1:
                mymazhaFragment.onRefresh();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onItemClicked(int position) {
        mycontext.toast("amuseFragment 点击了item" + position + "传递的分类id是: " + position);
        List<AmuseDetail> list = mAdapter.getMdatas();
        if (list != null && list.size() > 0) {
            //添加一个标记位，在activity中据此做switch case的区分(多个数据引入bundle)
            Intent intent = new Intent(mycontext, AmuseContainerActivity.class);
            AmuseDetail bean = list.get(position);
            //还需要传递关联关系
            intent.putExtra(AmuseFragment.EXTRAL_DATA, bean);
            intent.putExtra(BaseActivity.PAGETO, AmuseCommentFragment.TAG);
            mycontext.startActivity(intent);

        }
    }

    @Override
    public void onRefresh() {
        updateFromNet();

    }

    //从网络中抓取数据，更新UI
    private void updateFromNet() {
        //查询多条数据
        BmobQuery<AmuseDetail> query = new BmobQuery<AmuseDetail>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, AmuseDetail.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.setLimit(10);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        query.findObjects(mycontext, new FindListener<AmuseDetail>() {
            @Override
            public void onSuccess(List<AmuseDetail> object) {
                // TODO Auto-generated method stub
                datas = new ArrayList<AmuseDetail>();
                if (datas != null) {
                    datas.clear();
                }
                for (AmuseDetail bean : object) {
                    //数据源填充
                    datas.add(bean);
                }
                mAdapter.setMdatas(datas);
//                mHandler.sendEmptyMessage(0);
//                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                BmobUtils.log(msg + "首页新闻");
                // TODO Auto-generated method stub
//                mHandler.sendEmptyMessage(0);
//                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    if (mSwipeLayout != null) {
//
//                        if (mSwipeLayout.isRefreshing()) {
//                            mSwipeLayout.setRefreshing(false);
//                        }
//                    }

                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //出生时辰
            case R.id.container_homepage_birthtime:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        mycontext, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(tv_homepage_birthtime);
                break;
            //跳转到江湖
            case R.id.container_toamuse:
                Intent intentamuse = new Intent(mycontext, MainActivity.class);
                intentamuse.putExtra(BaseActivity.PAGETO, AmuseFragment.TAG);
                mycontext.startActivity(intentamuse);
                mycontext.finish();
                break;
            //断网提醒
            case R.id.btn_nonet:
                //重新加载网络，有网络后按钮消失
                updateFromNet();
                if (NetUtil.CheckNetState()) {
                    btn_nonet.setVisibility(View.GONE);
                }
                break;
            //点击测算过的人 按钮
            case R.id.btn_history1:
                toMingpan();
                break;
            //点击测算过的人 按钮
            case R.id.btn_history2:
                toMingpan();
                break;
            //点击测算过的人 按钮
            case R.id.btn_history3:
                toMingpan();
                break;
            //点击测算过的人 按钮
            case R.id.btn_history4:
                toMingpan();
                break;
            //点击测算过的人 按钮
            case R.id.btn_history5:
                toMingpan();
                break;

            //出生年月
            case R.id.container_homepage_birthday:
                DateTimePickDialogUtil dateTimePicKDialog1 = new DateTimePickDialogUtil(
                        mycontext, initEndDateTime);
                dateTimePicKDialog1.dateTimePicKDialog(tv_birthday);
                break;
//            性别
            case R.id.container_homepage_sex:
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
                            input_sex.setText("男");
                        } else if (selectedIndex == 2) {
                            input_sex.setText("女");
                        }
                    }
                });

                new AlertDialog.Builder(mycontext)
                        .setTitle("选择性别")
                        .setView(outerView)
                        .setPositiveButton("OK", null)
                        .show();
                break;
//            时辰类型
            case R.id.container_homepage_datetype:
                View outerView1 = LayoutInflater.from(mycontext).inflate(R.layout.wheel_view, null);
                WheelView wv2 = (WheelView) outerView1.findViewById(R.id.wheel_view_wv);
                wv2.setOffset(1);
                wv2.setItems(Arrays.asList(TYPES));
                wv2.setSeletion(2);
                wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.e("datas", "selectedIndex" + selectedIndex + "   item" + item);
                        if (selectedIndex == 1) {
                            input_datetype.setText("阳历");
                        } else if (selectedIndex == 2) {
                            input_datetype.setText("阴历");
                        }
                    }
                });

                new AlertDialog.Builder(mycontext)
                        .setTitle("选择日期类型")
                        .setView(outerView1)
                        .setPositiveButton("OK", null)
                        .show();

                break;
//            立即排盘  跳转+隐藏popup

            case R.id.btn_homepage_divinate:
                //传值
                String username = null, sex = null, datetype = null, birthday = null, birthtime = null;
                if (TextUtils.isEmpty(input_name.getText().toString().trim())) {
                    username = "某人";
                } else {
                    username = input_name.getText().toString().trim();
                }
                if (TextUtils.isEmpty(input_sex.getText().toString().trim())) {
                    mycontext.toast("请选择性别");
                } else {
                    sex = input_sex.getText().toString().trim();

                }

                if (TextUtils.isEmpty(input_datetype.getText().toString().trim())) {
                    mycontext.toast("请选择日期类型");
                    return;
                } else {
                    datetype = input_datetype.getText().toString().trim();
                }

                if (tv_birthday != null) {
                    if (TextUtils.isEmpty(tv_birthday.getText().toString().trim())) {
                        mycontext.toast("请选择出生日期");
                        return;
                    } else {
                        birthday = tv_birthday.getText().toString().trim();
                    }
                }
                if (tv_homepage_birthtime != null) {
                    if (TextUtils.isEmpty(tv_homepage_birthtime.getText().toString().trim())) {
                        mycontext.toast("请选择出生时辰");
                        return;
                    } else {
                        birthtime = tv_homepage_birthtime.getText().toString().trim();
                    }
                }
                Log.e("cesuan", "立即排盘传值" + username + sex + datetype + birthday + birthtime);
                toMingpan(); //跳转
                container_popup.setVisibility(View.GONE);
                //显示测算历史
                container_history_recorder.setVisibility(View.VISIBLE);
                cesuanNum++;
                String msg = null;
                btnCesuan(msg, cesuanNum, username);
                //清空测算过的人
                break;
            //测算按钮
            case R.id.btn_divination_homepage:
                if (!ispop) {
                    container_popup.setVisibility(View.VISIBLE);
                    btn_divination_homepage.setText("收起");
                    input_name.setBackgroundResource(R.drawable.shape_container);
                    input_name.setHint("请输入你的名字");
                    ispop = !ispop;
                } else if (ispop) {
                    btn_divination_homepage.setText("测算");
                    container_popup.setVisibility(View.GONE);
                    input_name.setBackgroundResource(R.drawable.shape_container_homepageinput);
                    input_name.setHint("请输入你的名字/时辰");
                    ispop = !ispop;
                }
                break;
        }
    }

    //------------抽取-----------------
    //测算按钮逻辑
    public void btnCesuan(String msg, int cesuanNum, String username) {
        if (cesuanNum == 1) {
            sharedPreferencesUtils.setParam(mycontext, "btn_history1", username);
            msg = (String) sharedPreferencesUtils.getParam(mycontext, "btn_history1", "");
            btn_history1.setText(msg);
            btn_history1.setVisibility(View.VISIBLE);
        }
        if (cesuanNum == 2) {
            sharedPreferencesUtils.setParam(mycontext, "btn_history2", username);
            msg = (String) sharedPreferencesUtils.getParam(mycontext, "btn_history2", "");
            btn_history2.setText(msg);
            btn_history2.setVisibility(View.VISIBLE);
        }
        if (cesuanNum == 3) {
            sharedPreferencesUtils.setParam(mycontext, "btn_history3", username);
            msg = (String) sharedPreferencesUtils.getParam(mycontext, "btn_history3", "");
            btn_history3.setText(msg);
            btn_history3.setVisibility(View.VISIBLE);
        }
        if (cesuanNum == 4) {
            sharedPreferencesUtils.setParam(mycontext, "btn_history4", username);
            msg = (String) sharedPreferencesUtils.getParam(mycontext, "btn_history4", "");
            btn_history4.setText(msg);
            btn_history4.setVisibility(View.VISIBLE);
        }
        if (cesuanNum == 5) {
            sharedPreferencesUtils.setParam(mycontext, "btn_history5", username);
            msg = (String) sharedPreferencesUtils.getParam(mycontext, "btn_history5", "");
            btn_history5.setText(msg);
            btn_history5.setVisibility(View.VISIBLE);
        }
    }

    //测算过的人缓存
    public void hascache() {
        if (sharedPreferencesUtils.getParam(mycontext, "btn_history1", "") != null) {
            container_history_recorder.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sharedPreferencesUtils.getParam(mycontext, "btn_history1", "").toString())) {
                btn_history1.setText(sharedPreferencesUtils.getParam(mycontext, "btn_history1", "").toString());
                btn_history1.setVisibility(View.VISIBLE);
            }
        }
        if (sharedPreferencesUtils.getParam(mycontext, "btn_history2", "") != null) {
            container_history_recorder.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sharedPreferencesUtils.getParam(mycontext, "btn_history2", "").toString())) {
                btn_history2.setText(sharedPreferencesUtils.getParam(mycontext, "btn_history2", "").toString());
                btn_history2.setVisibility(View.VISIBLE);
            }
        }
        if (sharedPreferencesUtils.getParam(mycontext, "btn_history3", "") != null) {
            container_history_recorder.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sharedPreferencesUtils.getParam(mycontext, "btn_history3", "").toString())) {

                btn_history3.setText(sharedPreferencesUtils.getParam(mycontext, "btn_history3", "").toString());
                btn_history3.setVisibility(View.VISIBLE);
            }
        }
        if (sharedPreferencesUtils.getParam(mycontext, "btn_history4", "") != null) {
            container_history_recorder.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sharedPreferencesUtils.getParam(mycontext, "btn_history4", "").toString())) {

                btn_history4.setText(sharedPreferencesUtils.getParam(mycontext, "btn_history4", "").toString());
                btn_history4.setVisibility(View.VISIBLE);
            }
        }
        if (sharedPreferencesUtils.getParam(mycontext, "btn_history5", "") != null) {
            container_history_recorder.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sharedPreferencesUtils.getParam(mycontext, "btn_history5", "").toString())) {

                btn_history5.setText(sharedPreferencesUtils.getParam(mycontext, "btn_history5", "").toString());
                btn_history5.setVisibility(View.VISIBLE);
            }
        }
    }

    //跳转到命盘
    public void toMingpan() {
        Intent intent = new Intent(mycontext, HomepageContainerActivity.class);
        intent.putExtra(BaseActivity.PAGETO, MingpanFragment.TAG);
        mycontext.startActivity(intent);
    }

    //切换
    static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    //适配器
    class MyPagerAdapter extends FragmentPagerAdapter

    {
        private MainActivity activity;
        private List<Fragment> datas;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> datas, MainActivity activity) {
            super(fm);
            this.activity = activity;
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            Log.e("datas", datas.size() + "");
            return datas.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

        ////添加页卡 初始化item
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        //移除之前的viewpager数据 待缓存优化
        mFragmentList.clear();
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
    public void onResume() {
        super.onResume();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                recommendLookerFragment.onRefresh();
                break;
            case 1:
                mymazhaFragment.onRefresh();
                break;
        }
        onRefresh();
        if (!NetUtil.CheckNetState()) {
            btn_nonet.setVisibility(View.VISIBLE);
        }
    }

}