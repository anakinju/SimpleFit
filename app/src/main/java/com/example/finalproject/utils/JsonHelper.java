package com.example.finalproject.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON序列化/反序列化工具类
 * 用于将训练会话中的运动列表转换为JSON字符串存储到数据库
 * 使用Gson库进行JSON处理
 */
public class JsonHelper {
    // Gson实例（线程安全，可复用）
    private static final Gson gson = new Gson();
    
    /**
     * 将运动列表转换为JSON字符串
     * @param exercises 运动列表
     * @return JSON字符串
     */
    public static String toJson(List<WorkoutSession.SessionExercise> exercises) {
        return gson.toJson(exercises);
    }
    
    /**
     * 将JSON字符串转换为运动列表
     * @param json JSON字符串
     * @return 运动列表
     */
    public static List<WorkoutSession.SessionExercise> fromJson(String json) {
        Type listType = new TypeToken<List<WorkoutSession.SessionExercise>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}
