package com.example.dmr.medicalrep.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.model.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    List<Request> requests;

    public RequestAdapter(List<Request> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
