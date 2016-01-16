//package com.example.administrator.bazipaipan.chat.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.TextMessageBody;
//import com.example.administrator.bazipaipan.R;
//import com.example.administrator.bazipaipan.chat.model.MyChatMsg;
//
//import java.util.List;
//
///**
// * Created by 王中阳 on 2015/12/16.
// */
//public class MyChatMsgAdapterbackup extends RecyclerView.Adapter<MyChatMsgAdapterbackup.RecyclerHolder> {
//    /**
//     * 1 inflater
//     * 2context
//     * 3datas   get set方法
//     * --
//     * 4 构造器
//     * 5 data的 get set方法
//     * <p>
//     */
//    private LayoutInflater mInflater;
//    private Context mContext;
//    private List<MyChatMsg> mdatas;
//    private IClickListener mCallBack; //信息回调
//    private EMConversation conversation;
//
//    public MyChatMsgAdapterbackup(Context mContext, IClickListener mCallBack, EMConversation conversation) {
//        this.mInflater = LayoutInflater.from(mContext);
//        this.mContext = mContext;
//        this.mCallBack = mCallBack;
//        this.conversation = conversation;
//    }
//
//    public List<MyChatMsg> getMdatas() {
//        return mdatas;
//    }
//
//    public void setMdatas(List<MyChatMsg> mdatas) {
//        this.mdatas = mdatas;
//        notifyDataSetChanged(); //观察者模式
//    }
//
//    //通过inflater引入item布局文件
//    @Override
//    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //分情况显示
//        View view;
//        view = mInflater.inflate(R.layout.item_chatlooker, parent, false);
//        return new RecyclerHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        EMMessage message = conversation.getAllMessages().get(position);
//        TextMessageBody body = (TextMessageBody) message.getBody();
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            if (message.getType() == EMMessage.Type.TXT) {
//            }
//        }
//        //从数据源中抽取javabean   围观区的评论
//        MyChatMsg bean = mdatas.get(position);
//        if (bean == null) {
//            return;
//        }
////        holder.tv_chatlooker_name.setText(bean.getUsername());
////        holder.tv_chatlooker_comment.setText(bean.getUsername() + "评论");
//
//        //----------------------------------
//    }
//
//    @Override
//    public int getItemCount() {
////        if (mdatas != null && mdatas.size() > 0) {
////            return mdatas.size();
////        }
////        return 0;
//        return conversation.getAllMsgCount();
//    }
//
//    class RecyclerHolder extends RecyclerView.ViewHolder {
//        TextView tv_chatlooker_name, tv_chatlooker_comment;
//
//        public RecyclerHolder(View itemView) {
//            super(itemView);
//            tv_chatlooker_name = (TextView) itemView.findViewById(R.id.tv_chatlooker_name);
//            tv_chatlooker_comment = (TextView) itemView.findViewById(R.id.tv_chatlooker_comment);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mCallBack.onItemClicked(getAdapterPosition());
//                }
//            });
//        }
//    }
//
//    public interface IClickListener {
//        void onItemClicked(int position);
//    }
//}
