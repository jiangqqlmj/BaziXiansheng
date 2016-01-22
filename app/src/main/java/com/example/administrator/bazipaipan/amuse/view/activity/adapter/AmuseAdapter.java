package com.example.administrator.bazipaipan.amuse.view.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.amuse.view.activity.model.AmuseCategory;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AmuseAdapter extends RecyclerView.Adapter<AmuseAdapter.RecyclerHolder> {
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
    private List<AmuseCategory> mdatas;
    private IClickListener mCallBack; //信息回调

    public AmuseAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<AmuseCategory> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<AmuseCategory> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_amuse_first, parent, false);
        return new RecyclerHolder(view);
    }

    String imageUrl;

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        AmuseCategory bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        holder.amuse_category_look_num.setText(bean.getAmuseLookerNum());
        holder.amuse_title.setText(bean.getAmuseTitle());
<<<<<<< HEAD
        if(bean.getAmuseImage()!=null) {
            imageUrl = bean.getAmuseImage().getUrl();
            //网络请求
            if (imageUrl != null) {
                if (holder.amuse_bg_img.getTag() != null && imageUrl.equals(holder.amuse_bg_img.getTag())) {
                    bean.getAmuseImage().loadImageThumbnail(mContext, holder.amuse_bg_img, 80, 80, 100);
                }
            } else {
                holder.amuse_bg_img.setImageResource(R.drawable.amuse_category_bg);
            }
=======
//        imageUrl = bean.getAmuseImage().getUrl();
        //网络请求
        if (bean.getAmuseImage() != null) {
//            if (holder.amuse_bg_img.getTag() != null && imageUrl.equals(holder.amuse_bg_img.getTag())) {
            bean.getAmuseImage().loadImageThumbnail(mContext, holder.amuse_bg_img, 80, 80, 100);
//            }
        } else {
            holder.amuse_bg_img.setImageResource(R.drawable.amuse_category_bg);
>>>>>>> wangzhongyang00/master
        }
        //通过viewholder设置属性值
    }

    @Override
    public int getItemCount() {
        if (mdatas != null && mdatas.size() > 0) {
            return mdatas.size();
        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView amuse_title, amuse_category_look_num;
        ImageView amuse_bg_img, amuse_forebg;

        public RecyclerHolder(View itemView) {
            super(itemView);
            amuse_title = (TextView) itemView.findViewById(R.id.tv_amuse_title_category);
            amuse_category_look_num = (TextView) itemView.findViewById(R.id.looker_num);
            amuse_bg_img = (ImageView) itemView.findViewById(R.id.iv_bg_amuse_category);
            amuse_forebg = (ImageView) itemView.findViewById(R.id.amuse_forebg);
            amuse_bg_img.setTag(imageUrl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onItemClicked(getAdapterPosition());
                }
            });

            //点击交互效果
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            amuse_forebg.setVisibility(View.VISIBLE);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            amuse_forebg.setVisibility(View.GONE);
                            break;
                        case MotionEvent.ACTION_UP:
                            amuse_forebg.setVisibility(View.GONE);
                            break;
                    }
                    return false;
                }
            });
        }
    }

    //回调的接口
    public interface IClickListener {
        void onItemClicked(int position);
    }
}
