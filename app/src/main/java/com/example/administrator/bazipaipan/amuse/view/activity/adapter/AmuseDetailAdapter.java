package com.example.administrator.bazipaipan.amuse.view.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseDetail;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseDetailAdapter extends RecyclerView.Adapter<AmuseDetailAdapter.RecyclerHolder> {
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
    private List<AmuseDetail> mdatas;
    private IClickListener mCallBack; //信息回调

    public AmuseDetailAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<AmuseDetail> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<AmuseDetail> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_amuse_second, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        AmuseDetail bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        //通过viewholder设置属性值  TextUtils为空判断
        if (bean.getAmuseCategory_pointer() != null) {
            holder.detail_title.setText(bean.getAmuseCategory_pointer().getAmuseTitle());  //需要关联
            holder.detail_looker_num.setText(bean.getAmuseCategory_pointer().getAmuseLookerNum());  //需要关联
        }
        holder.detail_introduce.setText(bean.getAmuseIntro());
        holder.detail_comment_num.setText(bean.getAmuseCommentNum());
        if (position % 4 == 0) {
            holder.container_amsue_second.setBackgroundColor(Color.rgb(0, 0, 0));
        } else if (position % 4 == 1) {
            holder.container_amsue_second.setBackgroundColor(Color.rgb(223, 187, 139));
        } else if (position % 4 == 2) {
            holder.container_amsue_second.setBackgroundColor(Color.rgb(145, 148, 191));
        } else if (position % 4 == 3) {
            holder.container_amsue_second.setBackgroundColor(Color.rgb(127, 185, 152));

        }
    }

    @Override
    public int getItemCount() {
        if (mdatas != null && mdatas.size() > 0) {
            return mdatas.size();
        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView detail_title, detail_introduce, detail_comment_num, detail_looker_num;
        LinearLayout container_amsue_second;
        //背景色待优化

        public RecyclerHolder(View itemView) {
            super(itemView);
            detail_title = (TextView) itemView.findViewById(R.id.title_amuse_second);
            detail_introduce = (TextView) itemView.findViewById(R.id.tv_introduce_amuse_second);
            detail_comment_num = (TextView) itemView.findViewById(R.id.tv_comment_num_amuse_second);
            detail_looker_num = (TextView) itemView.findViewById(R.id.looker_num_amuse_second);
            container_amsue_second = (LinearLayout) itemView.findViewById(R.id.container_amuse_second);

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
