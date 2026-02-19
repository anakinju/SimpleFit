package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.adapter.SubCategoryAdapter;
import com.example.finalproject.data.FitnessDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子分类选择界面Activity
 * 显示指定训练部位（Upper Body / Lower Body / Core）下的所有子分类
 * 每个子分类显示图标、描述和运动数量
 */
public class SubCategoryActivity extends AppCompatActivity implements SubCategoryAdapter.OnSubCategoryClickListener {
    
    private RecyclerView recyclerViewSubCategories;
    private SubCategoryAdapter adapter;
    private MaterialToolbar toolbar;
    private FitnessDatabase database;
    private String section;  // 训练部位（Upper Body / Lower Body / Core）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        section = getIntent().getStringExtra("section");
        database = FitnessDatabase.getDatabase(this);

        initViews();
        setupToolbar();
        loadSubCategories();
    }

    private void initViews() {
        recyclerViewSubCategories = findViewById(R.id.recyclerViewSubCategories);
        toolbar = findViewById(R.id.toolbar);

        recyclerViewSubCategories.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(section + " Categories");
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 加载子分类列表
     * 从数据库获取子分类，并为每个子分类创建显示项（包含图标、描述、数量）
     */
    private void loadSubCategories() {
        List<String> subCategoryNames = database.fitnessDao().getSubCategoriesBySection(section);
        List<SubCategoryAdapter.SubCategoryItem> subCategories = new ArrayList<>();
        
        // 获取子分类的描述和图标映射
        Map<String, String> categoryDescriptions = getCategoryDescriptions();
        Map<String, Integer> categoryIcons = getCategoryIcons();
        
        // 为每个子分类创建显示项
        for (String subCategory : subCategoryNames) {
            int exerciseCount = database.fitnessDao().getExercisesBySectionAndSubCategory(section, subCategory).size();
            String description = categoryDescriptions.getOrDefault(subCategory, "Training exercises");
            int iconResourceId = categoryIcons.getOrDefault(subCategory, R.drawable.ic_push_ups);
            
            subCategories.add(new SubCategoryAdapter.SubCategoryItem(
                subCategory, description, iconResourceId, exerciseCount));
        }
        
        adapter = new SubCategoryAdapter(subCategories, this);
        recyclerViewSubCategories.setAdapter(adapter);
        
        // 添加列表进入动画
        addListAnimation();
    }
    
    /**
     * 获取子分类的描述文本映射
     * @return 子分类名称 -> 描述文本的映射
     */
    private Map<String, String> getCategoryDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        // Upper Body
        descriptions.put("Push-ups", "Knee, Standard, Pike Push-ups");
        descriptions.put("Dips", "Ground, Assisted, Standard Dips");
        descriptions.put("Pull-ups", "Dead Hang, Assisted, Standard Pull-ups");
        // Lower Body
        descriptions.put("Lunges", "Standard, Weighted Lunges");
        descriptions.put("Squats", "Standard, Weighted Squats");
        descriptions.put("Bulgarian Split Squats", "Standard, Weighted Bulgarian Squats");
        // Core
        descriptions.put("Crunches", "Upper Abs Training");
        descriptions.put("Planks", "Full Core Stabilization");
        descriptions.put("Russian Twists", "Obliques Training");
        return descriptions;
    }
    
    /**
     * 获取子分类的图标资源ID映射
     * @return 子分类名称 -> 图标资源ID的映射
     */
    private Map<String, Integer> getCategoryIcons() {
        Map<String, Integer> icons = new HashMap<>();
        // Upper Body
        icons.put("Push-ups", R.drawable.ic_push_ups);
        icons.put("Dips", R.drawable.ic_dips);
        icons.put("Pull-ups", R.drawable.ic_pull_ups);
        // Lower Body
        icons.put("Lunges", R.drawable.ic_lunges);
        icons.put("Squats", R.drawable.ic_squats);
        icons.put("Bulgarian Split Squats", R.drawable.ic_bulgarian_split_squats);
        // Core
        icons.put("Crunches", R.drawable.ic_crunches);
        icons.put("Planks", R.drawable.ic_planks);
        icons.put("Russian Twists", R.drawable.ic_russian_twists);
        return icons;
    }

    /**
     * 子分类点击事件处理
     * 跳转到运动列表界面，显示该子分类下的所有运动
     * @param subCategory 子分类名称
     */
    @Override
    public void onSubCategoryClick(String subCategory) {
        Intent intent = new Intent(this, ExerciseListActivity.class);
        intent.putExtra("section", section);
        intent.putExtra("subCategory", subCategory);
        startActivity(intent);
        // 添加页面切换动画
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    
    /**
     * 添加列表进入动画
     * 使用淡入效果提升用户体验
     */
    private void addListAnimation() {
        recyclerViewSubCategories.setAlpha(0f);
        recyclerViewSubCategories.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(100)
                .start();
    }
}
