package com.example.administrator.bazipaipan.me.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.me.view.model.Recharge;
import com.example.administrator.bazipaipan.utils.MyStringUtils;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.RecyclerHolder> {
    /**
     * 1 inflater
     * 2context
     * 3datas   get set方法
     * --
     * 4 构造器
     * 5 data的 get set方法
     */
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Recharge> mdatas;
    private IClickListener mCallBack; //信息回调

    public RechargeAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<Recharge> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<Recharge> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recharge, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        Recharge bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(bean.getRechargeNum()))) {
            holder.tv_rechargenum.setText("0");
        } else {
            holder.tv_rechargenum.setText(String.valueOf(bean.getRechargeNum()));
        }

        if (TextUtils.isEmpty(String.valueOf(bean.getCoinNum()))) {
            holder.tv_coinnum.setText("0");
        } else {
            holder.tv_coinnum.setText(MyStringUtils.cutout(bean.getCoinNum()));
        }

        //----------------------------------
    }

    @Override
    public int getItemCount() {
        if (mdatas != null && mdatas.size() > 0) {
            return mdatas.size();
        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tv_rechargenum, tv_coinnum;

        public RecyclerHolder(View itemView) {
            super(itemView);
            tv_rechargenum = (TextView) itemView.findViewById(R.id.tv_rechargenum);
            tv_coinnum = (TextView) itemView.findViewById(R.id.tv_coinnum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface IClickListener {
        void onItemClicked(int position);
    }
}
