package com.example.finalproject.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 运动数据实体类
 * 使用Room数据库存储运动信息，包括名称、分类、难度、MET值等
 */
@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;                    // 运动ID（主键，自增）
    private String name;               // 运动名称（如：Standard Push-up）
    private String section;            // 运动部位（Upper Body / Lower Body / Core）
    private String subCategory;        // 子分类（如：Push-ups, Dips, Pull-ups, Lunges, Squats等）
    private String difficulty;         // 难度等级（Easy / Medium / Hard）
    private double metValue;           // MET值（代谢当量，用于计算卡路里）
    private String videoUrl;           // GIF文件名（用于演示动画）
    private String exerciseType;       // 运动类型：counter（计数器）或timer（计时器）
    private String targetGoal;         // 目标数量/时间（用于核心训练，如"Target: 20 reps"）
    private String targetMuscles;      // 目标肌肉群（如："Chest + Arms + Shoulders"）
    private String trainingTips;       // 训练要点（新手友好的英文指导）

    /**
     * 构造函数
     * @param name 运动名称
     * @param section 运动部位
     * @param subCategory 子分类
     * @param difficulty 难度等级
     * @param metValue MET值
     * @param videoUrl GIF文件名
     * @param exerciseType 运动类型（counter/timer）
     * @param targetGoal 目标数量/时间
     * @param targetMuscles 目标肌肉群
     * @param trainingTips 训练要点
     */
    public Exercise(String name, String section, String subCategory, String difficulty, double metValue, String videoUrl, String exerciseType, String targetGoal, String targetMuscles, String trainingTips) {
        this.name = name;
        this.section = section;
        this.subCategory = subCategory;
        this.difficulty = difficulty;
        this.metValue = metValue;
        this.videoUrl = videoUrl;
        this.exerciseType = exerciseType;
        this.targetGoal = targetGoal;
        this.targetMuscles = targetMuscles;
        this.trainingTips = trainingTips;
    }

    // ========== Getter和Setter方法 ==========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public double getMetValue() { return metValue; }
    public void setMetValue(double metValue) { this.metValue = metValue; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }

    public String getTargetGoal() { return targetGoal; }
    public void setTargetGoal(String targetGoal) { this.targetGoal = targetGoal; }

    public String getTargetMuscles() { return targetMuscles; }
    public void setTargetMuscles(String targetMuscles) { this.targetMuscles = targetMuscles; }

    public String getTrainingTips() { return trainingTips; }
    public void setTrainingTips(String trainingTips) { this.trainingTips = trainingTips; }
}
