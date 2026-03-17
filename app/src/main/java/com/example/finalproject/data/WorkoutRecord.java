package com.example.finalproject.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 训练记录实体类
 * 存储用户每次训练会话的完整信息，包括时间戳、身体数据、总卡路里和运动列表
 */
@Entity(tableName = "workout_records")
public class WorkoutRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;                  // 记录ID（主键，自增）
    private long timestamp;          // 训练时间戳（毫秒）
    private double height;            // 用户身高（厘米）
    private double weight;            // 用户体重（公斤）
    private double totalCalories;     // 总消耗卡路里
    private String exercisesJson;     // 运动列表的JSON字符串（存储SessionExercise数组）

    public WorkoutRecord(long timestamp, double height, double weight, double totalCalories, String exercisesJson) {
        this.timestamp = timestamp;
        this.height = height;
        this.weight = weight;
        this.totalCalories = totalCalories;
        this.exercisesJson = exercisesJson;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }

    public String getExercisesJson() { return exercisesJson; }
    public void setExercisesJson(String exercisesJson) { this.exercisesJson = exercisesJson; }
}
