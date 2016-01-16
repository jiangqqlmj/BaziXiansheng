package com.example.administrator.bazipaipan.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.MyApplication;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.model.Gift;
import com.example.administrator.bazipaipan.utils.BmobUtils;
import com.example.administrator.bazipaipan.utils.MyStringUtils;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class ChatGiftAdapter extends RecyclerView.Adapter<ChatGiftAdapter.RecyclerHolder> {
    /**
     * 1 inflater
     * 2context
     * 3datas   get set方法
     * --
     * 4 构造器
     * 5 data的 get set方法
     * 聊天队列
     */
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Gift> mdatas;
    private IClickListener mCallBack; //信息回调

    public ChatGiftAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<Gift> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<Gift> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chatgift, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean   围观区的评论
        Gift bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        if (MyStringUtils.cutout(bean.getGiftPrice()).equals("0")) {
            holder.tv_giftprice.setText("免费");
        } else {
            holder.tv_giftprice.setText(MyStringUtils.cutout(bean.getGiftPrice()) + "币");
        }
        holder.tv_giftname.setText(bean.getGiftName());
        if (bean.getGiftImage() != null) {
            bean.getGiftImage().loadImageThumbnail(mContext, holder.iv_chatgift_head, 80, 80, 100);
        } else {
            holder.iv_chatgift_head.setImageResource(R.drawable.norfoshouzhua);
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
        ImageView iv_chatgift_head;
        TextView tv_giftname, tv_giftprice;

        public RecyclerHolder(View itemView) {
            super(itemView);
            iv_chatgift_head = (ImageView) itemView.findViewById(R.id.iv_chatgift_head);
            tv_giftname = (TextView) itemView.findViewById(R.id.tv_giftname);
            tv_giftprice = (TextView) itemView.findViewById(R.id.tv_giftprice);
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
