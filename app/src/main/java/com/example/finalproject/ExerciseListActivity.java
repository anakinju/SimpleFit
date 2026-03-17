package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.adapter.ExerciseAdapter;
import com.example.finalproject.data.Exercise;
import com.example.finalproject.data.FitnessDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity implements ExerciseAdapter.OnExerciseClickListener {
    
    private RecyclerView recyclerViewExercises;
    private ExerciseAdapter adapter;
    private MaterialToolbar toolbar;
    private FloatingActionButton fabWorkoutSummary;
    private FitnessDatabase database;
    private String section;
    private String subCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        section = getIntent().getStringExtra("section");
        subCategory = getIntent().getStringExtra("subCategory");
        database = FitnessDatabase.getDatabase(this);

        initViews();
        setupToolbar();
        loadExercises();
    }

    private void initViews() {
        recyclerViewExercises = findViewById(R.id.recyclerViewExercises);
        toolbar = findViewById(R.id.toolbar);
        fabWorkoutSummary = findViewById(R.id.fabWorkoutSummary);

        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        
        fabWorkoutSummary.setOnClickListener(v -> openWorkoutSummary());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String title = subCategory != null ? subCategory + " Exercises" : section + " Exercises";
            getSupportActionBar().setTitle(title);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadExercises() {
        List<Exercise> exercises;
        if (subCategory != null) {
            // 按子分类和难度排序获取运动
            exercises = database.fitnessDao().getExercisesBySectionAndSubCategory(section, subCategory);
        } else {
            // 直接按部位获取运动
            exercises = database.fitnessDao().getExercisesBySection(section);
        }
        adapter = new ExerciseAdapter(exercises, this);
        recyclerViewExercises.setAdapter(adapter);
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra("exerciseId", exercise.getId());
        startActivity(intent);
    }
    
    private void openWorkoutSummary() {
        Intent intent = new Intent(this, WorkoutSummaryActivity.class);
        startActivity(intent);
    }
}
