package com.example.administrator.bazipaipan.me.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bazipaipan.BaseActivity;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.login.model.MyUser;
import com.example.administrator.bazipaipan.me.MeContainerActivity;
import com.example.administrator.bazipaipan.me.view.adapter.RechargeAdapter;
import com.example.administrator.bazipaipan.me.view.model.Recharge;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.example.administrator.bazipaipan.widget.DividerItemDecoration;
import com.example.administrator.bazipaipan.widget.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class RechargeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RechargeAdapter.IClickListener {
    public static final String TAG = "RechargeFragment";
    private MeContainerActivity mycontext;
    //2view对象
    private RecyclerView recyclerView;
    //3数据源
    private List<Recharge> datas;
    //4适配器
    private RechargeAdapter mAdapter;
    //5网络加载更多
    private VerticalSwipeRefreshLayout mSwipeLayout;
    //常量传递
    public static final String PAYGOODS = "paygoods";
    public static final String PAYMONEY = "paymoney";
    @InjectView(R.id.current_gold_num)
    TextView current_gold_num;


    //单例思想获得实例对象
    public RechargeFragment() {
    }

    public static RechargeFragment newInstance() {
        RechargeFragment fragment = new RechargeFragment();
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
        initViews();
        updateGoldNum();

    }

    private void initViews() {
        current_gold_num = (TextView) mycontext.findViewById(R.id.current_gold_num);
    }


    //----------------------------
    private void updateGoldNum() {
        //缓存策略，先网络再本地
        String uid = BmobUtils.getCurrentId(mycontext);
        BmobQuery<MyUser> bmobQuery = new BmobQuery<MyUser>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = bmobQuery.hasCachedResult(mycontext, MyUser.class);
//        if (isCache) {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        bmobQuery.getObject(mycontext, uid, new GetListener<MyUser>() {
            @Override
            public void onSuccess(MyUser myUser) {
                current_gold_num.setText(myUser.getGoldNum() + "");
            }

            @Override
            public void onFailure(int i, String s) {
                BmobUtils.log("充值金币" + s);
            }
        });


    }

    //---------------------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recharge, container, false);
        ButterKnife.inject(mycontext, view);

        updateUI(view);
        return view;
    }

    private void updateUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recharge);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //子布局装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new RechargeAdapter(mycontext, this);
        //适配器未装填内容
        datas = new ArrayList<>();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        //网络刷新
        onRefresh();
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
        updateGoldNum();
    }

    private void updateFromNet() {
        //查询多条数据
        BmobQuery<Recharge> query = new BmobQuery<Recharge>();
        //实例化数据源
        datas = new ArrayList<Recharge>();
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(mycontext, Recharge.class);
//        if (isCache) {
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        query.findObjects(mycontext, new FindListener<Recharge>() {
            @Override
            public void onSuccess(List<Recharge> object) {
                // TODO Auto-generated method stub
                datas.clear(); //清空原数据之后加载
                for (Recharge bean : object) {
                    //数据源填充
                    datas.add(bean);
                }
                mAdapter.setMdatas(datas);
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.e("datas", "充值" + msg);
                mHandler.sendEmptyMessage(0);
                mSwipeLayout.setRefreshing(false);
            }
        });
    }


    //点击item的跳转逻辑
    @Override
    public void onItemClicked(int position) {
        datas = mAdapter.getMdatas();
        Intent intent = new Intent(mycontext, MeContainerActivity.class);
        intent.putExtra(BaseActivity.PAGETO, PayFragment.TAG);
        intent.putExtra(PAYGOODS, datas.get(position).getCoinNum());
        intent.putExtra(PAYMONEY, datas.get(position).getRechargeNum());
        getActivity().startActivity(intent);
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

}
