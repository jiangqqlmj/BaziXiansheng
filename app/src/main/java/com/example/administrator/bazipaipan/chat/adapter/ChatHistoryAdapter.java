package com.example.administrator.bazipaipan.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.login.model.MyUser;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.RecyclerHolder> {
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
    private List<MyUser> mdatas;
    private IClickListener mCallBack; //信息回调

    public ChatHistoryAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<MyUser> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<MyUser> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chathistory, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean   围观区的评论
        MyUser bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        //暂时写死
        if (bean.getAvatar() != null) {
            bean.getAvatar().loadImageThumbnail(mContext, holder.iv_chathistory_head, 80, 80, 100);
        } else {
            holder.iv_chathistory_head.setImageResource(R.drawable.augur_head);
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
        ImageView iv_chathistory_head;

        public RecyclerHolder(View itemView) {
            super(itemView);
            iv_chathistory_head = (ImageView) itemView.findViewById(R.id.iv_chathistory_head);
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
