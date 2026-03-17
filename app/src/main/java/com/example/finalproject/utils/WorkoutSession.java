package com.example.finalproject.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 训练会话管理器
 * 使用静态List存储当前训练会话中的所有运动
 * 简化版本：不使用单例模式，直接使用静态方法
 */
public class WorkoutSession {
    // 当前训练会话中的运动列表
    private static List<SessionExercise> exercises = new ArrayList<>();
    
    /**
     * 训练会话中的单个运动记录
     * 包含运动信息、次数/时长、MET值以及辅助/负重信息
     */
    public static class SessionExercise {
        public int exerciseId;         // 运动ID
        public String exerciseName;     // 运动名称
        public int reps;                // 重复次数（计数器模式）
        public int duration;            // 持续时间（秒，计时器模式）
        public double metValue;          // MET值（用于计算卡路里）
        public double assistWeight;      // 辅助重量（公斤）- 用于Assisted练习，从体重中减去
        public double addedWeight;      // 负重（公斤）- 用于Weighted练习，加到体重上
        
        /**
         * 构造函数 - 标准运动（无辅助/负重）
         */
        public SessionExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.reps = reps;
            this.duration = duration;
            this.metValue = metValue;
            this.assistWeight = 0.0;  // 默认无辅助重量
            this.addedWeight = 0.0;   // 默认无负重
        }
        
        /**
         * 构造函数 - 辅助运动（如Assisted Dips, Assisted Pull-up）
         */
        public SessionExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue, double assistWeight) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.reps = reps;
            this.duration = duration;
            this.metValue = metValue;
            this.assistWeight = assistWeight;
            this.addedWeight = 0.0;   // 默认无负重
        }
        
        /**
         * 构造函数 - 负重运动（如Weighted Lunge, Weighted Squat）
         */
        public SessionExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue, double assistWeight, double addedWeight) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.reps = reps;
            this.duration = duration;
            this.metValue = metValue;
            this.assistWeight = assistWeight;
            this.addedWeight = addedWeight;
        }
    }
    
    /**
     * 添加标准运动到训练会话
     */
    public static void addExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue) {
        exercises.add(new SessionExercise(exerciseId, exerciseName, reps, duration, metValue));
    }
    
    /**
     * 添加辅助运动到训练会话（辅助重量会从体重中减去）
     */
    public static void addAssistedExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue, double assistWeight) {
        exercises.add(new SessionExercise(exerciseId, exerciseName, reps, duration, metValue, assistWeight));
    }
    
    /**
     * 添加负重运动到训练会话（负重会加到体重上）
     */
    public static void addWeightedExercise(int exerciseId, String exerciseName, int reps, int duration, double metValue, double addedWeight) {
        exercises.add(new SessionExercise(exerciseId, exerciseName, reps, duration, metValue, 0.0, addedWeight));
    }
    
    /**
     * 获取当前训练会话中的所有运动（返回副本，防止外部修改）
     */
    public static List<SessionExercise> getExercises() {
        return new ArrayList<>(exercises);
    }
    
    /**
     * 清空训练会话
     */
    public static void clearSession() {
        exercises.clear();
    }
    
    /**
     * 检查训练会话是否为空
     */
    public static boolean isEmpty() {
        return exercises.isEmpty();
    }
}
