package com.example.administrator.bazipaipan.augur.view.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMGroupInfo;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.augur.view.fragment.model.Augur;
import com.example.administrator.bazipaipan.utils.MyStringUtils;

import java.util.List;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class AugurAdapter extends RecyclerView.Adapter<AugurAdapter.RecyclerHolder> {
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
    private List<EMGroupInfo> chat_mdatas;
    private IClickListener mCallBack; //信息回调

    public AugurAdapter(Context mContext, IClickListener mCallBack) {
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

    public List<EMGroupInfo> getChat_mdatas() {
        return chat_mdatas;
    }

    public void setChat_mdatas(List<EMGroupInfo> chat_mdatas) {
        this.chat_mdatas = chat_mdatas;
        notifyDataSetChanged(); //观察者模式
    }

    //通过inflater引入item布局文件
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_augur_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        //环信
        //根据群聊ID从服务器获取群聊基本信息
        EMGroupInfo emGroupInfo = chat_mdatas.get(position);
        for (int i = 0; i < chat_mdatas.size(); i++) {
            Log.e("chat", chat_mdatas.get(position).getGroupName() + "  群聊组名" + position);
        }
        if (chat_mdatas.get(position).getGroupName() != null) {
            if (TextUtils.isEmpty(chat_mdatas.get(position).getGroupName())) {
                //查询环信数据表
                holder.augur_name.setText("无名大师");
            } else {
                //算命先生的名字有坑
                holder.augur_name.setText(position + chat_mdatas.get(position).getGroupName());
            }
        } else {
            holder.augur_name.setText("无名大师");
        }
//
//        EMGroup group;
//        try {
//            group = EMGroupManager.getInstance().getGroupFromServer(emGroupInfo.getGroupId());
//            Log.e("chat", group + " is group");
//            //保存获取下来的群聊基本信息
//            EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
//            group.getMembers();//获取群成员
//            group.getOwner();//获取群主
//
//        } catch (EaseMobException e) {
//            e.printStackTrace();
//        }

//        从数据源中抽取javabean
        if (mdatas == null) {
            return;
        }
        Augur bean = mdatas.get(position);
        if (bean == null) {
            return;
        }
        if (TextUtils.isEmpty(bean.getLookerNum() + "")) {
            holder.looker_num.setText("0");
        } else {
            holder.looker_num.setText(MyStringUtils.cutout(bean.getLookerNum()));
        }
//        if (bean.getAugur_pointer() != null) {
//            if (TextUtils.isEmpty(bean.getAugur_pointer().getUsername())) {
//                //查询环信数据表
//
//                holder.augur_name.setText("无名大师");
//            } else {
//                //算命先生的名字有坑
//                holder.augur_name.setText(bean.getUserId());
//            }
//        } else {
//            holder.augur_name.setText("无名大师");
//        }
        if (TextUtils.isEmpty(bean.getFocusNum() + "")) {
            holder.focus_num.setText("0");
        } else {
            holder.focus_num.setText(MyStringUtils.cutout(bean.getFocusNum()));
        }
        if (bean.getAugur_pointer() != null) {

            if (TextUtils.isEmpty(bean.getAugur_pointer().getGoldNum() + "")) {
                holder.gold_num.setText("0");
            } else {
                holder.gold_num.setText(bean.getAugur_pointer().getGoldNum() + "");
            }
        } else {
            holder.gold_num.setText("0");
        }
        if (bean.getAugur_pointer() != null) {
            if (bean.getAugur_pointer().getAvatar() == null) {
                //图片暂时写死
                holder.iv_bg.setImageResource(R.drawable.augur_head);
            } else {
                //加载网络图片  没写
                bean.getAugur_pointer().getAvatar().loadImageThumbnail(mContext, holder.iv_bg, 150, 150, 100);
            }
        }
        //测算状态标记为判断  ing done
        if (bean.getAugur_pointer() != null) {
            if (bean.getAugur_pointer().getOnlineStatus() != null) {
                if (bean.getAugur_pointer().getOnlineStatus()) {
                    holder.is_divinating.setImageResource(R.drawable.augur_status);
                } else {
                    holder.is_divinating.setImageResource(R.drawable.augur_status_nor);
                }
            }
        } else {
            holder.is_divinating.setImageResource(R.drawable.augur_status_nor);
        }
    }

    //    @Override
//    public int getItemCount() {
//        if (mdatas != null && mdatas.size() > 0) {
//            return mdatas.size();
//        }
//        return 0;
//    }
    @Override
    public int getItemCount() {
        if (chat_mdatas != null && mdatas != null) {
            if (chat_mdatas.size() < mdatas.size()) {
                return chat_mdatas.size();
            } else {
                return mdatas.size();
            }
        }
//        if (chat_mdatas != null && chat_mdatas.size() > 0) {
//            return chat_mdatas.size();
//        }
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView looker_num, augur_name, focus_num, gold_num;
        ImageView iv_bg, is_divinating;

        public RecyclerHolder(View itemView) {
            super(itemView);

            looker_num = (TextView) itemView.findViewById(R.id.augur_tv_looker_num);
            augur_name = (TextView) itemView.findViewById(R.id.augur_name);
            focus_num = (TextView) itemView.findViewById(R.id.augur_focus_num);
            gold_num = (TextView) itemView.findViewById(R.id.augur_gold_num);
            iv_bg = (ImageView) itemView.findViewById(R.id.item_augur_left_imageview);
            is_divinating = (ImageView) itemView.findViewById(R.id.augur_status);
            //点击回调的聊天逻辑
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
