package com.example.administrator.bazipaipan.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.chat.ChatContainerActivity;

/**
 * Created by 王中阳 on 2015/12/16.
 */
public class MyChatMsgAdapter extends BaseAdapter {
    private ChatContainerActivity mcontext;
    private EMConversation conversation;
    private TextView msg, username;

    public MyChatMsgAdapter(EMConversation conversation, ChatContainerActivity mcontext) {
        this.conversation = conversation;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return conversation.getAllMsgCount();
    }

    @Override
    public Object getItem(int position) {
        return conversation.getAllMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EMMessage message = conversation.getAllMessages().get(position);
        TextMessageBody body = (TextMessageBody) message.getBody();
        if (message.direct == EMMessage.Direct.RECEIVE) {//接收方
            if (message.getType() == EMMessage.Type.TXT) {
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_chatlooker, null);
                username = (TextView) convertView.findViewById(R.id.tv_chatusername);
                username.setText(message.getFrom());
            }
        } else {
            if (message.getType() == EMMessage.Type.TXT) {
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_chatmsg2, null);
                username = (TextView) convertView.findViewById(R.id.tv_chatusername);
                username.setText(message.getFrom());
            }
        }
        msg = (TextView) convertView.findViewById(R.id.tv_chatmsg);
        msg.setText(body.getMessage());
        return convertView;
    }
}
