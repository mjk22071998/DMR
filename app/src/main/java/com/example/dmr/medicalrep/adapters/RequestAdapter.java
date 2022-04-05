package com.example.dmr.medicalrep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    List<Request> requests;
    Context context;

    public RequestAdapter(List<Request> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new RequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
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

    public class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView to,from,time;
        RecyclerView meds;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            to=itemView.findViewById(R.id.to);
            from=itemView.findViewById(R.id.from);
            time=itemView.findViewById(R.id.time);
            meds=itemView.findViewById(R.id.meds);
        }
    }
}
