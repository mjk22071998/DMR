package com.example.dmr.medicalrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {
    Context context;
    List<Chat> chats;
    boolean rep;
    OnChatClickListener onChatClickListener;

    public interface OnChatClickListener {
        void onClick(int position);
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public ChatAdapter(Context context,List<Chat> chats, boolean rep) {
        this.context = context;
        this.chats = chats;
        this.rep = rep;
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        if (rep){
            holder.sender.setText(chats.get(position).getDocName());
        } else {
            holder.sender.setText(chats.get(position).getRepName());
        }
        if (position%3==0)
            holder.avatar.setColorFilter(context.getResources().getColor(R.color.blue));
        else if (position%3==1)
            holder.avatar.setColorFilter(context.getResources().getColor(R.color.green));
        else
            holder.avatar.setColorFilter(context.getResources().getColor(R.color.pink));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ChatVH extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView sender;
        public ChatVH(@NonNull View itemView) {
            super(itemView);
            avatar=itemView.findViewById(R.id.avatar);
            sender=itemView.findViewById(R.id.sender);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChatClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
