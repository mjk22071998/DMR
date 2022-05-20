package com.example.dmr.medicalrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Message;
import com.example.dmr.medicalrep.utils.SessionManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageVH> {
    List<Message> messages;
    Context context;

    public MessageAdapter(Context context,List<Message> messages) {
        this.context=context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
            return new MessageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_msg,parent,false));
        else
            return new MessageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageVH holder, int position) {
        holder.message.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        if (SessionManager.getUser(context).get(SessionManager.CNIC).toString().equals(messages.get(position).getSender()))
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageVH extends RecyclerView.ViewHolder{
        TextView message;
        public MessageVH(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.msg);
        }
    }
}
