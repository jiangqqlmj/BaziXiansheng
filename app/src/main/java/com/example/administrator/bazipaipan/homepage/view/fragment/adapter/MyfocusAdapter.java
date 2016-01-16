package com.example.administrator.bazipaipan.homepage.view.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.augur.model.Augur;
import com.example.administrator.bazipaipan.utils.MyStringUtils;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MyfocusAdapter extends RecyclerView.Adapter<MyfocusAdapter.RecyclerHolder> {
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
    private List<Augur> mdatas;
    private IClickListener mCallBack; //信息回调

    public MyfocusAdapter(Context mContext, IClickListener mCallBack) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCallBack = mCallBack;
    }

    public List<Augur> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<Augur> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_augur_recyclervier_vertical, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //从数据源中抽取javabean
        Augur bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        if (TextUtils.isEmpty(bean.getLookerNum() + "")) {
            holder.tv_look.setText("0");
        } else {
            holder.tv_look.setText(MyStringUtils.cutout(bean.getLookerNum()));
        }
//       名字
        if (bean.getAugur_pointer() != null) {
            if (TextUtils.isEmpty(bean.getAugur_pointer().getUsername())) {
                holder.tv_augurName.setText("无名大师");
            } else {
                //算命先生的名字有坑
                holder.tv_augurName.setText(bean.getUserId());
            }
        } else {
            holder.tv_augurName.setText("无名大师");
        }
        //金钱
        if (bean.getAugur_pointer()==null){
            holder.tv_money.setText("0");
        }else {

            if (TextUtils.isEmpty(bean.getAugur_pointer().getGoldNum() + "")) {
                holder.tv_money.setText("0");
            } else {
                holder.tv_money.setText(MyStringUtils.cutout(bean.getAugur_pointer().getGoldNum()));
            }
        }
        //头像
        if (bean.getAugur_pointer() != null) {
            if (bean.getAugur_pointer().getAvatar() == null) {
                //图片暂时写死
                holder.iv_head.setImageResource(R.drawable.augur_head);
            } else {
                //加载网络图片  没写
                bean.getAugur_pointer().getAvatar().loadImageThumbnail(mContext, holder.iv_head, 150, 150, 100);
            }
        }
//        holder.iv_cancel_focus.setImageResource(R.drawable.cancel_focus);
        //测算状态标记为判断  ing done
        if (bean.getAugur_pointer() != null) {
            if (bean.getAugur_pointer().getOnlineStatus() != null) {
                if (bean.getAugur_pointer().getOnlineStatus()) {
                    holder.iv_state.setImageResource(R.drawable.augur_status);
                } else {
                    holder.iv_state.setImageResource(R.drawable.augur_status_nor);
                }
            }
        } else {
            holder.iv_state.setImageResource(R.drawable.augur_status_nor);
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
        ImageView iv_head, iv_state;
        TextView tv_augurName, tv_guanzhu, tv_money, tv_look;


        public RecyclerHolder(View itemView) {
            super(itemView);
            iv_head = (ImageView) itemView.findViewById(R.id.item_home_left_imageview);
            iv_state = (ImageView) itemView.findViewById(R.id.home_augur_status);
            tv_augurName = (TextView) itemView.findViewById(R.id.home_augur_name);
            tv_guanzhu = (TextView) itemView.findViewById(R.id.home_focus_num);
            tv_money = (TextView) itemView.findViewById(R.id.home_gold_num);
            tv_look = (TextView) itemView.findViewById(R.id.home_tv_looker_num);
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
