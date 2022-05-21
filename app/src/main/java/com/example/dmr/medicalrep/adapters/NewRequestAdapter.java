package com.example.dmr.medicalrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Request;

import java.util.List;

public class NewRequestAdapter extends RecyclerView.Adapter<NewRequestAdapter.NewRequestViewHolder> {

    List<Request> requests;
    Context context;
    OnAcceptClickListener onAcceptClickListener;
    OnRejectClickListener onRejectClickListener;
    OnMessageClickListener onMessageClickListener;


    public void setOnAcceptClickListener(OnAcceptClickListener onAcceptClickListener) {
        this.onAcceptClickListener = onAcceptClickListener;
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    public void setOnRejectClickListener(OnRejectClickListener onRejectClickListener) {
        this.onRejectClickListener = onRejectClickListener;
    }

    public NewRequestAdapter(List<Request> requests) {
        this.requests = requests;
    }

    public interface OnAcceptClickListener{
        void onClick(int position);
    }

    public interface OnRejectClickListener{
        void onClick(int position);
    }

    public interface OnMessageClickListener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public NewRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new NewRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_request,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewRequestViewHolder holder, int position) {
        MedicineAdapter adapter;
        holder.from.setText(requests.get(position).getRepName());
        holder.to.setText(requests.get(position).getDocName());
        holder.time.setText(requests.get(position).getTimestamp().toString());
        adapter=new MedicineAdapter(requests.get(position).getMedicines());
        holder.meds.setLayoutManager(new LinearLayoutManager(context));
        holder.meds.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class NewRequestViewHolder extends RecyclerView.ViewHolder{
        TextView to,from,time;
        Button accept,reject,message;
        RecyclerView meds;

        public NewRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            to=itemView.findViewById(R.id.to);
            from=itemView.findViewById(R.id.from);
            time=itemView.findViewById(R.id.time);
            meds=itemView.findViewById(R.id.meds);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            message=itemView.findViewById(R.id.message);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAcceptClickListener.onClick(getAdapterPosition());
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRejectClickListener.onClick(getAdapterPosition());
                }
            });
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMessageClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
