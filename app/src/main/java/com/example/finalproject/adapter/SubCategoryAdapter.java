package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import java.util.List;
import java.util.Map;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    
    public static class SubCategoryItem {
        public String name;
        public String description;
        public int iconResourceId;
        public int exerciseCount;
        
        public SubCategoryItem(String name, String description, int iconResourceId, int exerciseCount) {
            this.name = name;
            this.description = description;
            this.iconResourceId = iconResourceId;
            this.exerciseCount = exerciseCount;
        }
    }
    
    private List<SubCategoryItem> subCategories;
    private OnSubCategoryClickListener listener;
    
    public interface OnSubCategoryClickListener {
        void onSubCategoryClick(String subCategory);
    }
    
    public SubCategoryAdapter(List<SubCategoryItem> subCategories, OnSubCategoryClickListener listener) {
        this.subCategories = subCategories;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category, parent, false);
        return new SubCategoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategoryItem item = subCategories.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return subCategories.size();
    }
    
    class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSubCategoryIcon;
        private TextView tvSubCategoryName, tvSubCategoryDescription, tvExerciseCount;
        
        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSubCategoryIcon = itemView.findViewById(R.id.ivSubCategoryIcon);
            tvSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
            tvSubCategoryDescription = itemView.findViewById(R.id.tvSubCategoryDescription);
            tvExerciseCount = itemView.findViewById(R.id.tvExerciseCount);
            
            itemView.setOnClickListener(v -> {
                // 添加点击动画
                v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start();
                        
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onSubCategoryClick(subCategories.get(position).name);
                        }
                    })
                    .start();
            });
        }
        
        public void bind(SubCategoryItem item) {
            ivSubCategoryIcon.setImageResource(item.iconResourceId);
            tvSubCategoryName.setText(item.name);
            tvSubCategoryDescription.setText(item.description);
            tvExerciseCount.setText(item.exerciseCount + " exercises");
        }
    }
}
