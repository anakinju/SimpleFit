package com.example.finalproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * 数据库访问对象（DAO）
 * 定义所有数据库操作的接口方法
 */
@Dao
public interface FitnessDao {
    // ========== 运动数据相关查询 ==========
    
    /**
     * 插入单个运动
     */
    @Insert
    void insertExercise(Exercise exercise);

    /**
     * 批量插入运动列表
     */
    @Insert
    void insertExercises(List<Exercise> exercises);

    /**
     * 根据运动部位获取所有运动
     * @param section 运动部位（Upper Body / Lower Body / Core）
     */
    @Query("SELECT * FROM exercises WHERE section = :section")
    List<Exercise> getExercisesBySection(String section);

    /**
     * 获取指定部位的所有子分类
     * @param section 运动部位
     * @return 子分类名称列表（去重）
     */
    @Query("SELECT DISTINCT subCategory FROM exercises WHERE section = :section")
    List<String> getSubCategoriesBySection(String section);

    /**
     * 根据部位和子分类获取运动列表，按难度排序（Easy -> Medium -> Hard）
     * @param section 运动部位
     * @param subCategory 子分类
     */
    @Query("SELECT * FROM exercises WHERE section = :section AND subCategory = :subCategory ORDER BY CASE difficulty WHEN 'Easy' THEN 1 WHEN 'Medium' THEN 2 WHEN 'Hard' THEN 3 END")
    List<Exercise> getExercisesBySectionAndSubCategory(String section, String subCategory);

    /**
     * 根据ID获取运动
     * @param id 运动ID
     */
    @Query("SELECT * FROM exercises WHERE id = :id")
    Exercise getExerciseById(int id);

    /**
     * 获取运动总数（用于判断是否需要初始化数据）
     */
    @Query("SELECT COUNT(*) FROM exercises")
    int getExerciseCount();
    
    /**
     * 更新运动的MET值（用于数据同步）
     * @param section 运动部位
     * @param subCategory 子分类
     * @param difficulty 难度等级
     * @param metValue 新的MET值
     */
    @Query("UPDATE exercises SET metValue = :metValue WHERE section = :section AND subCategory = :subCategory AND difficulty = :difficulty")
    void updateExerciseMetValue(String section, String subCategory, String difficulty, double metValue);
    
    /**
     * 更新运动的GIF文件名（用于修复映射问题）
     * @param name 运动名称
     * @param section 运动部位
     * @param videoUrl 新的GIF文件名
     */
    @Query("UPDATE exercises SET videoUrl = :videoUrl WHERE name = :name AND section = :section")
    void updateExerciseVideoUrl(String name, String section, String videoUrl);

    // ========== 训练记录相关查询 ==========
    
    /**
     * 插入训练记录
     * @param workoutRecord 训练记录对象
     * @return 插入记录的ID
     */
    @Insert
    long insertWorkoutRecord(WorkoutRecord workoutRecord);

    /**
     * 获取所有训练记录（按时间倒序）
     * @return 训练记录列表
     */
    @Query("SELECT * FROM workout_records ORDER BY timestamp DESC")
    List<WorkoutRecord> getAllWorkoutRecords();

    /**
     * 分页查询训练记录（性能优化）
     * @param limit 每页数量
     * @param offset 偏移量
     * @return 训练记录列表
     */
    @Query("SELECT * FROM workout_records ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    List<WorkoutRecord> getWorkoutRecordsPaged(int limit, int offset);
    
    /**
     * 获取训练记录总数
     * @return 记录总数
     */
    @Query("SELECT COUNT(*) FROM workout_records")
    int getWorkoutRecordsCount();
}
