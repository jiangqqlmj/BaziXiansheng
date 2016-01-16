package com.example.administrator.bazipaipan.homepage.view.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseDetail;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class NewsAmuseAdapter extends RecyclerView.Adapter<NewsAmuseAdapter.RecyclerHolder> {
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

    public NewsAmuseAdapter(Context mContext, IClickListener mCallBack) {
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
        View view = mInflater.inflate(R.layout.item_amuse_homepage, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        AmuseDetail bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        //通过viewholder设置属性值   TextUtils
        holder.title.setText(bean.getAmuseDetailTitle());
        holder.introcude.setText(bean.getAmuseIntro());
    }

    @Override
    public int getItemCount() {
        if (mdatas != null && mdatas.size() > 0) {
            return mdatas.size();
        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView title, introcude;

        public RecyclerHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_home_amuse_title);
            introcude = (TextView) itemView.findViewById(R.id.tv_home_amuse_introduce);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface IClickListener {
        //tab点击切换
//        void onTabSelected(TabLayout.Tab tab);

        void onItemClicked(int position);
    }
}
