package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.data.Exercise;
import com.example.finalproject.utils.WorkoutSession;
import java.util.List;

public class WorkoutExerciseAdapter extends RecyclerView.Adapter<WorkoutExerciseAdapter.WorkoutExerciseViewHolder> {
    
    public static class WorkoutExerciseItem {
        public Exercise exercise;
        public WorkoutSession.SessionExercise sessionExercise;
        public double calories;
        
        public WorkoutExerciseItem(Exercise exercise, WorkoutSession.SessionExercise sessionExercise, double calories) {
            this.exercise = exercise;
            this.sessionExercise = sessionExercise;
            this.calories = calories;
        }
    }
    
    private List<WorkoutExerciseItem> items;
    
    public WorkoutExerciseAdapter(List<WorkoutExerciseItem> items) {
        this.items = items;
    }
    
    @NonNull
    @Override
    public WorkoutExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_exercise, parent, false);
        return new WorkoutExerciseViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull WorkoutExerciseViewHolder holder, int position) {
        WorkoutExerciseItem item = items.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    class WorkoutExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWorkoutExerciseName, tvWorkoutExerciseDetails, tvWorkoutCalories;
        
        public WorkoutExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutExerciseName = itemView.findViewById(R.id.tvWorkoutExerciseName);
            tvWorkoutExerciseDetails = itemView.findViewById(R.id.tvWorkoutExerciseDetails);
            tvWorkoutCalories = itemView.findViewById(R.id.tvWorkoutCalories);
        }
        
        public void bind(WorkoutExerciseItem item) {
            tvWorkoutExerciseName.setText(item.exercise.getName());
            
            StringBuilder details = new StringBuilder();
            if (item.sessionExercise.duration > 0) {
                details.append(item.sessionExercise.duration).append("s");
            }
            if (item.sessionExercise.reps > 0) {
                if (details.length() > 0) details.append(" | ");
                details.append(item.sessionExercise.reps).append("reps");
            }
            if (item.sessionExercise.assistWeight > 0) {
                if (details.length() > 0) details.append(" | ");
                details.append("Assist: ").append(String.format("%.1fkg", item.sessionExercise.assistWeight));
            }
            if (item.sessionExercise.addedWeight > 0) {
                if (details.length() > 0) details.append(" | ");
                details.append("Weight: +").append(String.format("%.1fkg", item.sessionExercise.addedWeight));
            }
            tvWorkoutExerciseDetails.setText(details.toString());
            
            tvWorkoutCalories.setText(String.format("%.1f kcal", item.calories));
        }
    }
}
