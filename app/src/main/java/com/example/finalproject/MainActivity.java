package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.utils.ExerciseDataLoader;
import com.example.finalproject.utils.UserProfileManager;
import com.google.android.material.card.MaterialCardView;

/**
 * 主界面Activity
 * 显示三个主要训练部位（上肢、下肢、核心）的入口卡片
 * 处理首次启动检查和用户信息显示
 */
public class MainActivity extends AppCompatActivity {
    
    // UI组件
    private UserProfileManager profileManager;
    private MaterialCardView cardUpperBody, cardLowerBody, cardCore;
    private Button btnHistory;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        profileManager = new UserProfileManager(this);
        
        // 检查是否是第一次启动，如果是则跳转到设置界面
        if (profileManager.isFirstLaunch()) {
            Intent intent = new Intent(this, ProfileSetupActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.activity_main);
        
        // 预加载运动数据到数据库
        ExerciseDataLoader.preloadExercises(this);
        
        initViews();
        setupClickListeners();
    }
    
    /**
     * 初始化UI组件
     */
    private void initViews() {
        cardUpperBody = findViewById(R.id.cardUpperBody);
        cardLowerBody = findViewById(R.id.cardLowerBody);
        cardCore = findViewById(R.id.cardCore);
        btnHistory = findViewById(R.id.btnHistory);
        tvWelcome = findViewById(R.id.tvWelcome);
        
        updateUserInfo();
    }
    
    /**
     * 更新用户信息显示
     */
    private void updateUserInfo() {
        double height = profileManager.getHeight();
        double weight = profileManager.getWeight();
        tvWelcome.setText(String.format("Height: %.0fcm | Weight: %.0fkg", height, weight));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 当从其他Activity返回时更新用户信息（可能用户修改了体重）
        updateUserInfo();
    }
    
    /**
     * 设置点击监听器
     */
    private void setupClickListeners() {
        cardUpperBody.setOnClickListener(v -> openExerciseList("Upper Body"));
        cardLowerBody.setOnClickListener(v -> openExerciseList("Lower Body"));
        cardCore.setOnClickListener(v -> openExerciseList("Core"));
        btnHistory.setOnClickListener(v -> openHistory());
    }
    
    /**
     * 打开训练部位的子分类选择界面
     * @param section 训练部位（Upper Body / Lower Body / Core）
     */
    private void openExerciseList(String section) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("section", section);
        startActivity(intent);
    }
    
    /**
     * 打开训练历史界面
     */
    private void openHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}
