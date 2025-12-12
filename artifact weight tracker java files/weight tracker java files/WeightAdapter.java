package com.example.weight_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
//array list for displaying weight history, ordered by date
public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private ArrayList<WeightData> weightList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WeightData weightData);
    }

    public WeightAdapter(ArrayList<WeightData> weightList, OnItemClickListener listener) {
        this.weightList = weightList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight, parent, false);
        return new WeightViewHolder(view, listener, weightList);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightData weightData = weightList.get(position);
        holder.tvWeight.setText("Weight: " + weightData.getWeight() + " lbs");
        holder.tvDate.setText("Date: " + weightData.getDate());
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }

    public static class WeightViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeight, tvDate;

        public WeightViewHolder(@NonNull View itemView, OnItemClickListener listener, ArrayList<WeightData> weightList) {
            super(itemView);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(weightList.get(position));
                }
            });
        }
    }
}
