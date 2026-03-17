package com.example.finalproject.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

/**
 * Room数据库类
 * 使用单例模式管理数据库实例
 * 
 * 数据库版本历史：
 * - version 10: 更新训练要点为简单英文（新手友好）
 * - version 9: 添加训练要点字段
 * - version 8: 添加子分类、运动类型、目标肌肉群等字段
 */
@Database(
    entities = {Exercise.class, WorkoutRecord.class},
    version = 10,
    exportSchema = false
)
public abstract class FitnessDatabase extends RoomDatabase {
    /**
     * 获取DAO接口
     */
    public abstract FitnessDao fitnessDao();

    // 单例实例（使用volatile确保线程安全）
    private static volatile FitnessDatabase INSTANCE;

    /**
     * 获取数据库实例（单例模式）
     * @param context 应用上下文
     * @return 数据库实例
     */
    public static FitnessDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FitnessDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FitnessDatabase.class, "fitness_database")
                            .allowMainThreadQueries()  // 允许主线程查询（简化demo，生产环境应使用后台线程）
                            .fallbackToDestructiveMigration()  // 版本升级时删除旧数据（demo模式）
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
