package com.example.finalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.adapter.HistoryAdapter;
import com.example.finalproject.data.FitnessDatabase;
import com.example.finalproject.data.WorkoutRecord;
import com.example.finalproject.utils.JsonHelper;
import com.example.finalproject.utils.WorkoutSession;
import com.example.finalproject.utils.UserProfileManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnItemClickListener {
    
    private RecyclerView recyclerViewHistory;
    private TextView tvEmptyHistory;
    private MaterialToolbar toolbar;
    private FloatingActionButton fabEditProfile;
    
    private HistoryAdapter adapter;
    private FitnessDatabase database;
    private UserProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        database = FitnessDatabase.getDatabase(this);
        profileManager = new UserProfileManager(this);

        initViews();
        setupToolbar();
        setupFab();
        loadHistory();
    }

    private void initViews() {
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        toolbar = findViewById(R.id.toolbar);
        fabEditProfile = findViewById(R.id.fabEditProfile);

        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Workout History");
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupFab() {
        fabEditProfile.setOnClickListener(v -> showEditProfileDialog());
    }

    private void loadHistory() {
        // 性能优化：在后台线程加载数据
        new Thread(() -> {
            List<WorkoutRecord> workoutRecords = database.fitnessDao().getAllWorkoutRecords();
            
            runOnUiThread(() -> {
                if (workoutRecords.isEmpty()) {
                    showEmptyState();
                } else {
                    showHistoryState();
                    adapter = new HistoryAdapter(workoutRecords, this);
                    recyclerViewHistory.setAdapter(adapter);
                }
            });
        }).start();
    }
    
    private void showEmptyState() {
        tvEmptyHistory.setVisibility(View.VISIBLE);
        recyclerViewHistory.setVisibility(View.GONE);
    }
    
    private void showHistoryState() {
        tvEmptyHistory.setVisibility(View.GONE);
        recyclerViewHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewDetailsClick(WorkoutRecord record) {
        showWorkoutDetailsDialog(record);
    }

    private void showEditProfileDialog() {
        try {
            // 创建简单的输入对话框
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            final EditText etHeight = new EditText(this);
            etHeight.setHint("Height (cm)");
            etHeight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etHeight.setText(String.valueOf((int) profileManager.getHeight()));
            
            final EditText etWeight = new EditText(this);
            etWeight.setHint("Weight (kg)");
            etWeight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etWeight.setText(String.valueOf((int) profileManager.getWeight()));

            layout.addView(etHeight);
            layout.addView(etWeight);

            new AlertDialog.Builder(this)
                    .setTitle("Edit Profile")
                    .setView(layout)
                    .setPositiveButton("Save", (dialog, which) -> {
                        try {
                            String heightStr = etHeight.getText().toString().trim();
                            String weightStr = etWeight.getText().toString().trim();
                            
                            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            
                            float height = Float.parseFloat(heightStr);
                            float weight = Float.parseFloat(weightStr);
                            
                            if (height <= 0 || weight <= 0) {
                                Toast.makeText(this, "Please enter positive numbers", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            
                            profileManager.saveProfile(height, weight);
                            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            
                            // Refresh the history
                            loadHistory();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setCancelable(true)
                    .show();
                    
        } catch (Exception e) {
            Toast.makeText(this, "Error opening edit dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showWorkoutDetailsDialog(WorkoutRecord record) {
        List<WorkoutSession.SessionExercise> exercises = JsonHelper.fromJson(record.getExercisesJson());
        StringBuilder details = new StringBuilder();
        details.append("Workout Date: ").append(new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.ENGLISH).format(new java.util.Date(record.getTimestamp()))).append("\n\n");
        details.append("Total Calories: ").append(String.format("%.1f kcal", record.getTotalCalories())).append("\n");
        details.append("Height: ").append(String.format("%.0fcm", record.getHeight())).append("\n");
        details.append("Weight: ").append(String.format("%.0fkg", record.getWeight())).append("\n\n");
        details.append("Exercises:\n");
        
        for (WorkoutSession.SessionExercise exercise : exercises) {
            details.append("• ").append(exercise.exerciseName).append(" - ");
            if (exercise.reps > 0) {
                details.append(exercise.reps).append(" reps");
            } else {
                details.append(exercise.duration).append(" seconds");
            }
            details.append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Workout Details")
                .setMessage(details.toString())
                .setPositiveButton("Close", null)
                .show();
    }
}
