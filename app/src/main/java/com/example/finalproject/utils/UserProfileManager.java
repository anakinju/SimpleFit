package com.example.finalproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户资料管理器
 * 使用SharedPreferences存储用户的身高、体重和首次启动标志
 */
public class UserProfileManager {
    // SharedPreferences文件名和键名常量
    private static final String PREFS_NAME = "FitnessProfile";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    private SharedPreferences prefs;

    /**
     * 构造函数
     * @param context 应用上下文
     */
    public UserProfileManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 检查是否是首次启动
     * @return true表示首次启动，需要显示设置界面
     */
    public boolean isFirstLaunch() {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    /**
     * 标记首次启动已完成
     */
    public void setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    /**
     * 保存用户资料
     * @param height 身高（厘米）
     * @param weight 体重（公斤）
     */
    public void saveProfile(double height, double weight) {
        prefs.edit()
                .putFloat(KEY_HEIGHT, (float) height)
                .putFloat(KEY_WEIGHT, (float) weight)
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply();
    }

    /**
     * 获取用户身高
     * @return 身高（厘米），默认170cm
     */
    public double getHeight() {
        return prefs.getFloat(KEY_HEIGHT, 170.0f);
    }

    /**
     * 获取用户体重
     * @return 体重（公斤），默认70kg
     */
    public double getWeight() {
        return prefs.getFloat(KEY_WEIGHT, 70.0f);
    }

    /**
     * 检查是否已有用户资料
     * @return true表示已有资料
     */
    public boolean hasProfile() {
        return prefs.contains(KEY_HEIGHT) && prefs.contains(KEY_WEIGHT);
    }
}
