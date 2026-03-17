package com.example.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.finalproject.data.Exercise;
import com.example.finalproject.data.FitnessDatabase;
import com.example.finalproject.utils.WorkoutSession;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

/**
 * 运动详情界面Activity
 * 显示运动的详细信息、GIF演示、训练要点
 * 支持计数器模式和计时器模式输入
 * 支持辅助重量和负重输入（用于Assisted/Weighted运动）
 */
public class ExerciseDetailActivity extends AppCompatActivity {
    
    // UI组件 - 基本信息
    private TextView tvExerciseName, tvTargetMuscles, tvMetValue, tvGifPlaceholder, tvTargetGoal, tvTrainingTips;
    private LinearLayout layoutTargetGoal, layoutAssistWeight, layoutAddedWeight, layoutTrainingTips;
    private Chip chipDifficulty;
    private ImageView imageViewDemo;
    private TextInputEditText etReps, etDuration, etAssistWeight, etAddedWeight;
    private Button btnAddToWorkout;
    private MaterialToolbar toolbar;
    
    // UI组件 - 计时器相关
    private LinearLayout layoutCounter, layoutTimer, layoutManualTime;
    private TextView tvTimerDisplay;
    private Button btnStartPause, btnReset;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;
    private int timerSeconds = 0;
    private Runnable timerRunnable;
    
    // 数据
    private Exercise exercise;
    private FitnessDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        database = FitnessDatabase.getDatabase(this);
        int exerciseId = getIntent().getIntExtra("exerciseId", -1);
        
        // 从数据库获取运动信息
        if (exerciseId != -1) {
            exercise = database.fitnessDao().getExerciseById(exerciseId);
        }

        initViews();
        setupToolbar();
        setupExerciseInfo();
        setupGifDemo();
        setupInputMode();
        setupAssistWeightInput();
    }

    /**
     * 初始化所有UI组件并设置监听器
     */
    private void initViews() {
        tvExerciseName = findViewById(R.id.tvExerciseName);
        tvTargetMuscles = findViewById(R.id.tvTargetMuscles);
        tvMetValue = findViewById(R.id.tvMetValue);
        tvTargetGoal = findViewById(R.id.tvTargetGoal);
        layoutTargetGoal = findViewById(R.id.layoutTargetGoal);
        tvTrainingTips = findViewById(R.id.tvTrainingTips);
        layoutTrainingTips = findViewById(R.id.layoutTrainingTips);
        chipDifficulty = findViewById(R.id.chipDifficulty);
        imageViewDemo = findViewById(R.id.imageViewDemo);
        tvGifPlaceholder = findViewById(R.id.tvGifPlaceholder);
        etReps = findViewById(R.id.etReps);
        etDuration = findViewById(R.id.etDuration);
        etAssistWeight = findViewById(R.id.etAssistWeight);
        layoutAssistWeight = findViewById(R.id.layoutAssistWeight);
        etAddedWeight = findViewById(R.id.etAddedWeight);
        layoutAddedWeight = findViewById(R.id.layoutAddedWeight);
        btnAddToWorkout = findViewById(R.id.btnAddToWorkout);
        toolbar = findViewById(R.id.toolbar);
        
        // 计时器相关
        layoutCounter = findViewById(R.id.layoutCounter);
        layoutTimer = findViewById(R.id.layoutTimer);
        layoutManualTime = findViewById(R.id.layoutManualTime);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnReset = findViewById(R.id.btnReset);

        btnAddToWorkout.setOnClickListener(v -> addToWorkout());
        btnStartPause.setOnClickListener(v -> toggleTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        
        setupTimerRunnable();
    }

    /**
     * 设置工具栏（返回按钮和标题）
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Exercise Details");
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 设置运动基本信息显示
     */
    private void setupExerciseInfo() {
        if (exercise != null) {
            tvExerciseName.setText(exercise.getName());
            tvTargetMuscles.setText(exercise.getTargetMuscles());
            tvMetValue.setText(String.format("%.1f", exercise.getMetValue()));
            chipDifficulty.setText(exercise.getDifficulty());
            
            // 显示训练目标（如果有的话）
            String targetGoal = exercise.getTargetGoal();
            if (targetGoal != null && !targetGoal.isEmpty()) {
                tvTargetGoal.setText(targetGoal);
                layoutTargetGoal.setVisibility(View.VISIBLE);
            } else {
                layoutTargetGoal.setVisibility(View.GONE);
            }
            
            // 显示训练要点
            String trainingTips = exercise.getTrainingTips();
            if (trainingTips != null && !trainingTips.isEmpty()) {
                tvTrainingTips.setText(trainingTips);
                layoutTrainingTips.setVisibility(View.VISIBLE);
            } else {
                layoutTrainingTips.setVisibility(View.GONE);
            }
            
            // 设置难度颜色
            switch (exercise.getDifficulty()) {
                case "Hard":
                    chipDifficulty.setChipBackgroundColorResource(R.color.difficulty_hard);
                    break;
                case "Medium":
                    chipDifficulty.setChipBackgroundColorResource(R.color.difficulty_medium);
                    break;
                case "Easy":
                    chipDifficulty.setChipBackgroundColorResource(R.color.difficulty_easy);
                    break;
            }
        }
    }

    /**
     * 设置GIF演示动画
     * 使用Glide库加载raw资源中的GIF文件
     */
    private void setupGifDemo() {
        try {
            // 使用exercise的videoUrl字段来加载相应的GIF
            String gifName = exercise.getVideoUrl();
            
            // 处理文件名映射（因为Android资源文件名必须全小写，但数据库存储的可能是其他格式）
            gifName = mapGifFileName(gifName);
            
            int gifResourceId = getResources().getIdentifier(gifName, "raw", getPackageName());
            
            if (gifResourceId != 0) {
                // 显示GIF，隐藏占位符
                imageViewDemo.setVisibility(View.VISIBLE);
                tvGifPlaceholder.setVisibility(View.GONE);
                
                // 使用Glide加载GIF，添加优化配置
                Glide.with(this)
                    .asGif()
                    .load(gifResourceId)
                    .fitCenter()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(imageViewDemo);
                    
            } else {
                // 没有找到GIF文件，显示占位符
                imageViewDemo.setVisibility(View.GONE);
                tvGifPlaceholder.setVisibility(View.VISIBLE);
                tvGifPlaceholder.setText("Demo GIF not available\nfor " + exercise.getName());
            }
        } catch (Exception e) {
            // 出错时显示占位符
            imageViewDemo.setVisibility(View.GONE);
            tvGifPlaceholder.setVisibility(View.VISIBLE);
            tvGifPlaceholder.setText("Error loading demo\n" + e.getMessage());
        }
    }
    
    /**
     * 映射GIF文件名以处理Android资源命名限制
     * 修复特定运动的GIF文件名映射问题
     * @param originalName 原始文件名
     * @return 映射后的文件名
     */
    private String mapGifFileName(String originalName) {
        switch (originalName) {
            case "knee_push_up":
                return "a2"; 
            case "standard_push_up":
                return "a1"; 
            case "pike_push_up":
                return "pike_push_up"; 
            default:
                return originalName;
        }
    }
    
    /**
     * 设置辅助重量/负重输入框
     * 根据运动类型（Assisted/Weighted）显示相应的输入框
     */
    private void setupAssistWeightInput() {
        String exerciseName = exercise.getName().toLowerCase();
        boolean isAssistedExercise = exerciseName.contains("assisted");
        boolean isWeightedExercise = exerciseName.contains("weighted");
        
        if (isAssistedExercise) {
            // 辅助运动：显示辅助重量输入框（从体重中减去）
            layoutAssistWeight.setVisibility(View.VISIBLE);
            layoutAddedWeight.setVisibility(View.GONE);
            // 根据具体运动设置提示文本
            if (exerciseName.contains("dip")) {
                etAssistWeight.setHint("Band/Block assistance (e.g., 10-20kg)");
            } else if (exerciseName.contains("pull")) {
                etAssistWeight.setHint("Band/Block assistance (e.g., 15-25kg)");
            }
        } else if (isWeightedExercise) {
            // 负重运动：显示负重输入框（加到体重上）
            layoutAssistWeight.setVisibility(View.GONE);
            layoutAddedWeight.setVisibility(View.VISIBLE);
            // 根据具体运动设置提示文本
            if (exerciseName.contains("lunge")) {
                etAddedWeight.setHint("Added weight (e.g., 10-30kg)");
            } else if (exerciseName.contains("squat")) {
                etAddedWeight.setHint("Added weight (e.g., 20-50kg)");
            } else if (exerciseName.contains("bulgarian")) {
                etAddedWeight.setHint("Added weight (e.g., 10-25kg)");
            }
        } else {
            // 标准运动：隐藏所有重量输入框
            layoutAssistWeight.setVisibility(View.GONE);
            layoutAddedWeight.setVisibility(View.GONE);
        }
    }

    /**
     * 将运动添加到今日训练会话
     * 处理计数器/计时器模式、辅助重量/负重的输入和验证
     */
    private void addToWorkout() {
        String exerciseType = exercise.getExerciseType();
        int reps = 0;
        int duration = 0;
        double assistWeight = 0.0;
        double addedWeight = 0.0;
        
        try {
            // 根据运动类型获取输入数据
            if ("counter".equals(exerciseType)) {
                // 计数器模式：获取重复次数
                String repsStr = etReps.getText().toString().trim();
                if (repsStr.isEmpty()) {
                    Toast.makeText(this, "Please enter repetitions", Toast.LENGTH_SHORT).show();
                    return;
                }
                reps = Integer.parseInt(repsStr);
                duration = reps * 2; // 估算：1次重复 = 2秒
            } else if ("timer".equals(exerciseType)) {
                // 计时器模式：优先使用计时器记录的时间，否则使用手动输入
                if (timerSeconds > 0) {
                    duration = timerSeconds;
                } else {
                    String durationStr = etDuration.getText().toString().trim();
                    if (durationStr.isEmpty()) {
                        Toast.makeText(this, "Please use timer or enter duration", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    duration = Integer.parseInt(durationStr);
                }
            }

            // 验证输入数据
            if (duration <= 0) {
                Toast.makeText(this, "Please enter valid workout data", Toast.LENGTH_SHORT).show();
                return;
            }

            // 获取辅助重量（如果有）
            boolean isAssistedExercise = exercise.getName().toLowerCase().contains("assisted");
            if (isAssistedExercise && layoutAssistWeight.getVisibility() == View.VISIBLE) {
                String assistWeightStr = etAssistWeight.getText().toString().trim();
                if (!assistWeightStr.isEmpty()) {
                    assistWeight = Double.parseDouble(assistWeightStr);
                    if (assistWeight < 0) {
                        Toast.makeText(this, "Assist weight cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            // 获取负重（如果有）
            boolean isWeightedExercise = exercise.getName().toLowerCase().contains("weighted");
            if (isWeightedExercise && layoutAddedWeight.getVisibility() == View.VISIBLE) {
                String addedWeightStr = etAddedWeight.getText().toString().trim();
                if (!addedWeightStr.isEmpty()) {
                    addedWeight = Double.parseDouble(addedWeightStr);
                    if (addedWeight < 0) {
                        Toast.makeText(this, "Added weight cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            // 根据运动类型添加到训练会话
            if (isAssistedExercise && assistWeight > 0) {
                WorkoutSession.addAssistedExercise(exercise.getId(), exercise.getName(), reps, duration, exercise.getMetValue(), assistWeight);
            } else if (isWeightedExercise && addedWeight > 0) {
                WorkoutSession.addWeightedExercise(exercise.getId(), exercise.getName(), reps, duration, exercise.getMetValue(), addedWeight);
            } else {
                WorkoutSession.addExercise(exercise.getId(), exercise.getName(), reps, duration, exercise.getMetValue());
            }
            
            Toast.makeText(this, "Added to today's workout", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置输入模式（计数器或计时器）
     * 根据运动的exerciseType字段显示相应的输入界面
     */
    private void setupInputMode() {
        if (exercise == null) return;
        
        String exerciseType = exercise.getExerciseType();
        if ("counter".equals(exerciseType)) {
            // 显示计数器模式
            layoutCounter.setVisibility(View.VISIBLE);
            layoutTimer.setVisibility(View.GONE);
            layoutManualTime.setVisibility(View.GONE);
        } else if ("timer".equals(exerciseType)) {
            // 显示计时器模式
            layoutCounter.setVisibility(View.GONE);
            layoutTimer.setVisibility(View.VISIBLE);
            layoutManualTime.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 设置计时器Runnable
     * 每秒更新一次计时器显示
     */
    private void setupTimerRunnable() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTimerRunning) {
                    timerSeconds++;
                    updateTimerDisplay();
                    timerHandler.postDelayed(this, 1000); // 1秒后再次执行
                }
            }
        };
    }
    
    /**
     * 切换计时器状态（开始/暂停）
     */
    private void toggleTimer() {
        if (isTimerRunning) {
            // 暂停计时器
            isTimerRunning = false;
            btnStartPause.setText("Start");
            timerHandler.removeCallbacks(timerRunnable);
        } else {
            // 开始计时器
            isTimerRunning = true;
            btnStartPause.setText("Pause");
            timerHandler.post(timerRunnable);
        }
    }
    
    /**
     * 重置计时器
     */
    private void resetTimer() {
        isTimerRunning = false;
        timerSeconds = 0;
        btnStartPause.setText("Start");
        timerHandler.removeCallbacks(timerRunnable);
        updateTimerDisplay();
    }
    
    /**
     * 更新计时器显示（格式：MM:SS）
     */
    private void updateTimerDisplay() {
        int minutes = timerSeconds / 60;
        int seconds = timerSeconds % 60;
        tvTimerDisplay.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
