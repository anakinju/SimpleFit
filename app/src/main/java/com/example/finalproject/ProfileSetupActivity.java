package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.utils.UserProfileManager;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileSetupActivity extends AppCompatActivity {
    
    private TextInputEditText etHeight, etWeight;
    private Button btnSaveProfile;
    private UserProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        profileManager = new UserProfileManager(this);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                return;
            }

            profileManager.saveProfile(height, weight);
            
            // 跳转到主界面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
