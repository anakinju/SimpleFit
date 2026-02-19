package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.adapter.WorkoutExerciseAdapter;
import com.example.finalproject.data.Exercise;
import com.example.finalproject.utils.JsonHelper;
import com.example.finalproject.data.FitnessDatabase;
import com.example.finalproject.data.WorkoutRecord;
import com.example.finalproject.utils.UserProfileManager;
import com.example.finalproject.utils.WorkoutSession;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

/**
 * 训练总结界面Activity
 * 显示今日训练会话中的所有运动、总卡路里消耗
 * 支持保存训练记录到数据库和清空当前会话
 */
public class WorkoutSummaryActivity extends AppCompatActivity {
    
    // UI组件
    private TextView tvTotalCalories, tvEmptyMessage;
    private RecyclerView recyclerViewWorkoutExercises;
    private Button btnClearWorkout, btnSaveWorkout;
    private MaterialToolbar toolbar;
    
    // 数据和适配器
    private WorkoutExerciseAdapter adapter;
    private FitnessDatabase database;
    private UserProfileManager profileManager;
    private List<WorkoutExerciseAdapter.WorkoutExerciseItem> workoutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        database = FitnessDatabase.getDatabase(this);
        profileManager = new UserProfileManager(this);

        initViews();
        setupToolbar();
        loadWorkoutData();
    }

    private void initViews() {
        tvTotalCalories = findViewById(R.id.tvTotalCalories);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        recyclerViewWorkoutExercises = findViewById(R.id.recyclerViewWorkoutExercises);
        btnClearWorkout = findViewById(R.id.btnClearWorkout);
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout);
        toolbar = findViewById(R.id.toolbar);

        recyclerViewWorkoutExercises.setLayoutManager(new LinearLayoutManager(this));
        
        btnClearWorkout.setOnClickListener(v -> clearWorkout());
        btnSaveWorkout.setOnClickListener(v -> saveWorkout());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Today's Workout Summary");
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 加载训练数据
     * 计算每个运动的卡路里消耗（考虑辅助重量/负重）
     * 显示总卡路里和运动列表
     */
    private void loadWorkoutData() {
        List<WorkoutSession.SessionExercise> exercises = WorkoutSession.getExercises();
        workoutItems = new ArrayList<>();
        
        // 检查训练会话是否为空
        if (WorkoutSession.isEmpty()) {
            showEmptyState();
            return;
        }
        
        double totalCalories = 0;
        double userWeight = profileManager.getWeight();
        
        // 遍历所有运动，计算卡路里
        for (WorkoutSession.SessionExercise sessionExercise : exercises) {
            // 计算有效体重：对于辅助练习，使用用户体重减去辅助重量；对于负重练习，使用用户体重加上负重
            double durationHours = sessionExercise.duration / 3600.0;
            double effectiveWeight = userWeight;
            
            // 辅助练习：减去辅助重量（至少保留10%体重，避免计算异常）
            if (sessionExercise.assistWeight > 0) {
                effectiveWeight = Math.max(userWeight - sessionExercise.assistWeight, 0.1 * userWeight);
            }
            // 负重练习：加上负重
            else if (sessionExercise.addedWeight > 0) {
                effectiveWeight = userWeight + sessionExercise.addedWeight;
            }
            
            // 卡路里计算公式：MET值 × 有效体重(kg) × 持续时间(小时)
            double calories = sessionExercise.metValue * effectiveWeight * durationHours;
            totalCalories += calories;
            
            // 创建一个简化的Exercise对象
            Exercise exercise = new Exercise(sessionExercise.exerciseName, "", "", "", sessionExercise.metValue, "", "", "", "", "");
            exercise.setId(sessionExercise.exerciseId);
            
            workoutItems.add(new WorkoutExerciseAdapter.WorkoutExerciseItem(
                exercise, sessionExercise, calories));
        }
        
        tvTotalCalories.setText(String.format("%.1f kcal", totalCalories));
        
        adapter = new WorkoutExerciseAdapter(workoutItems);
        recyclerViewWorkoutExercises.setAdapter(adapter);
        
        showWorkoutState();
    }
    
    private void showEmptyState() {
        tvEmptyMessage.setVisibility(View.VISIBLE);
        recyclerViewWorkoutExercises.setVisibility(View.GONE);
        tvTotalCalories.setText("0 kcal");
        btnSaveWorkout.setEnabled(false);
    }
    
    private void showWorkoutState() {
        tvEmptyMessage.setVisibility(View.GONE);
        recyclerViewWorkoutExercises.setVisibility(View.VISIBLE);
        btnSaveWorkout.setEnabled(true);
    }
    
    private void clearWorkout() {
        WorkoutSession.clearSession();
        loadWorkoutData();
        Toast.makeText(this, "Today's workout records cleared", Toast.LENGTH_SHORT).show();
    }
    
    private void saveWorkout() {
        if (WorkoutSession.isEmpty()) {
            Toast.makeText(this, "No workout records to save", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 计算总卡路里
        double totalCalories = 0;
        for (WorkoutExerciseAdapter.WorkoutExerciseItem item : workoutItems) {
            totalCalories += item.calories;
        }
        
        // 将运动列表转换为JSON
        List<WorkoutSession.SessionExercise> exercises = WorkoutSession.getExercises();
        String exercisesJson = JsonHelper.toJson(exercises);
        
        // 创建训练记录（简化版：直接包含JSON数据）
        WorkoutRecord workoutRecord = new WorkoutRecord(
            System.currentTimeMillis(),
            profileManager.getHeight(),
            profileManager.getWeight(),
            totalCalories,
            exercisesJson
        );
        
        // 插入训练记录
        database.fitnessDao().insertWorkoutRecord(workoutRecord);
        
        // 清空会话
        WorkoutSession.clearSession();
        
        Toast.makeText(this, "Workout record saved", Toast.LENGTH_SHORT).show();
        
        // 跳转到历史记录
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWorkoutData(); // 重新加载数据以反映最新变化
    }
}
