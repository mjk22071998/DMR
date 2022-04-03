package com.example.dmr.medicalrep.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Doctors;

import java.util.ArrayList;

public class DoctorAdapter extends Adapter<DoctorAdapter.DoctorViewHolder> {

    ArrayList<Doctors> doctors;
    OnDoctorClickListener onDoctorClickListener;

    public interface OnDoctorClickListener{
        void onClick(int position);
    }

    public void setOnDoctorClickListener(OnDoctorClickListener onDoctorClickListener) {
        this.onDoctorClickListener = onDoctorClickListener;
    }

    public DoctorAdapter(ArrayList<Doctors> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        holder.phone.setText(doctors.get(position).getPhoneNumber());
        holder.name.setText(doctors.get(position).getFullName());
        holder.city.setText(doctors.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorViewHolder extends ViewHolder{
        public TextView name,city,phone;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.docName);
            city=itemView.findViewById(R.id.docCity);
            phone=itemView.findViewById(R.id.docPhone);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDoctorClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
