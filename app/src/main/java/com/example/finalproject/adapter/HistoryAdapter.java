package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.data.WorkoutRecord;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    
    public interface OnItemClickListener {
        void onViewDetailsClick(WorkoutRecord record);
    }
    
    private List<WorkoutRecord> workoutRecords;
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    
    public HistoryAdapter(List<WorkoutRecord> workoutRecords, OnItemClickListener listener) {
        this.workoutRecords = workoutRecords;
        this.listener = listener;
    }
    

    public void updateData(List<WorkoutRecord> newRecords) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new WorkoutRecordDiffCallback(workoutRecords, newRecords));
        workoutRecords = newRecords;
        diffResult.dispatchUpdatesTo(this);
    }
    
    private static class WorkoutRecordDiffCallback extends DiffUtil.Callback {
        private final List<WorkoutRecord> oldList;
        private final List<WorkoutRecord> newList;
        
        public WorkoutRecordDiffCallback(List<WorkoutRecord> oldList, List<WorkoutRecord> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }
        
        @Override
        public int getOldListSize() {
            return oldList.size();
        }
        
        @Override
        public int getNewListSize() {
            return newList.size();
        }
        
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }
        
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            WorkoutRecord oldRecord = oldList.get(oldItemPosition);
            WorkoutRecord newRecord = newList.get(newItemPosition);
            return oldRecord.getTimestamp() == newRecord.getTimestamp() &&
                   Math.abs(oldRecord.getTotalCalories() - newRecord.getTotalCalories()) < 0.1;
        }
    }
    
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WorkoutRecord record = workoutRecords.get(position);
        holder.bind(record);
    }
    
    @Override
    public int getItemCount() {
        return workoutRecords.size();
    }
    
    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHistoryDate, tvHistoryCalories, tvHistoryHeight, 
                        tvHistoryWeight, tvHistoryTime;
        private MaterialButton btnViewDetails;
        
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryDate = itemView.findViewById(R.id.tvHistoryDate);
            tvHistoryCalories = itemView.findViewById(R.id.tvHistoryCalories);
            tvHistoryHeight = itemView.findViewById(R.id.tvHistoryHeight);
            tvHistoryWeight = itemView.findViewById(R.id.tvHistoryWeight);
            tvHistoryTime = itemView.findViewById(R.id.tvHistoryTime);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
        
        public void bind(WorkoutRecord record) {
            Date date = new Date(record.getTimestamp());
            
            tvHistoryDate.setText(dateFormat.format(date));
            tvHistoryTime.setText(timeFormat.format(date));
            tvHistoryCalories.setText(String.format("%.1f kcal", record.getTotalCalories()));
            tvHistoryHeight.setText(String.format("%.0fcm", record.getHeight()));
            tvHistoryWeight.setText(String.format("%.0fkg", record.getWeight()));
            
            // Set click listener for view details button
            btnViewDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetailsClick(record);
                }
            });
        }
    }
}
