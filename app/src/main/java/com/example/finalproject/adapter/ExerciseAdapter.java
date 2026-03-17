package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.data.Exercise;
import com.google.android.material.chip.Chip;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    
    private List<Exercise> exercises;
    private OnExerciseClickListener listener;
    
    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }
    
    public ExerciseAdapter(List<Exercise> exercises, OnExerciseClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }
    
    @Override
    public int getItemCount() {
        return exercises.size();
    }
    
    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvExerciseName, tvExerciseMet;
        private Chip chipDifficulty;
        
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseMet = itemView.findViewById(R.id.tvExerciseMet);
            chipDifficulty = itemView.findViewById(R.id.chipDifficulty);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onExerciseClick(exercises.get(position));
                }
            });
        }
        
        public void bind(Exercise exercise) {
            tvExerciseName.setText(exercise.getName());
            tvExerciseMet.setText(String.format("MET: %.1f", exercise.getMetValue()));
            chipDifficulty.setText(exercise.getDifficulty());
            
            // 根据难度设置颜色
            switch (exercise.getDifficulty()) {
                case "Hard":
                    chipDifficulty.setChipBackgroundColorResource(android.R.color.holo_red_light);
                    break;
                case "Medium":
                    chipDifficulty.setChipBackgroundColorResource(android.R.color.holo_orange_light);
                    break;
                case "Easy":
                    chipDifficulty.setChipBackgroundColorResource(android.R.color.holo_green_light);
                    break;
            }
        }
    }
}
