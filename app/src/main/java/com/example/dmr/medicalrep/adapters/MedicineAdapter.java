package com.example.dmr.medicalrep.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.model.Medicine;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    List<Medicine> medicines;

    public MedicineAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicines,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        holder.description.setText(medicines.get(position).getDescription());
        holder.potency.setText(medicines.get(position).getPotency());
        holder.name.setText(medicines.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView name,potency,description;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.medName);
            potency=itemView.findViewById(R.id.medPotency);
            description=itemView.findViewById(R.id.medDesc);
        }
    }
}
