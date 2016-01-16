package com.example.administrator.bazipaipan.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.model.ChatContribute;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class ChatContributeAdapter extends RecyclerView.Adapter<ChatContributeAdapter.RecyclerHolder> {
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
    private List<ChatContribute> mdatas;
    private IClickListener mCallBack; //信息回调

    public ChatContributeAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<ChatContribute> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<ChatContribute> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chatcontribute, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        ChatContribute bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        //荣誉榜
        if (position == 0) {
            holder.iv_chatcontribute_Id.setImageResource(R.drawable.king1);
        } else if (position == 1) {
            holder.iv_chatcontribute_Id.setImageResource(R.drawable.king2);
        } else if (position == 2) {
            holder.iv_chatcontribute_Id.setImageResource(R.drawable.king3);
        } else {
            holder.iv_chatcontribute_Id.setImageResource(R.drawable.king4);
        }
        //
        holder.tv_chatcontribute_No.setText(String.valueOf(position + 1));
        holder.tv_chatcontribute_goldnum.setText(bean.getContributeNum());
        if (bean.getMyUser() == null) {
            return;
        }
        holder.tv_chatcontribute_username.setText(bean.getMyUser().getUsername());
    }

    @Override
    public int getItemCount() {
        if (mdatas != null && mdatas.size() > 0) {
            return mdatas.size();
        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tv_chatcontribute_No, tv_chatcontribute_username, tv_chatcontribute_goldnum;
        ImageView iv_chatcontribute_Id;

        public RecyclerHolder(View itemView) {
            super(itemView);
            tv_chatcontribute_No = (TextView) itemView.findViewById(R.id.tv_chatcontribute_No);
            tv_chatcontribute_username = (TextView) itemView.findViewById(R.id.tv_chatcontribute_username);
            tv_chatcontribute_goldnum = (TextView) itemView.findViewById(R.id.tv_chatcontribute_goldnum);
            iv_chatcontribute_Id = (ImageView) itemView.findViewById(R.id.iv_chatcontribute_Id);
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
