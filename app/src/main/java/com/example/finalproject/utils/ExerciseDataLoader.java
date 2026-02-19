package com.example.finalproject.utils;

import android.content.Context;
import com.example.finalproject.data.Exercise;
import com.example.finalproject.data.FitnessDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * 运动数据加载器
 * 负责初始化数据库中的运动数据，包括21个动作的完整信息
 * 支持数据更新和GIF映射修复
 */
public class ExerciseDataLoader {
    
    /**
     * 预加载运动数据
     * 如果数据库已有数据，则只更新MET值和GIF映射
     * 如果数据库为空，则插入所有21个动作的初始数据
     * 
     * @param context 应用上下文
     */
    public static void preloadExercises(Context context) {
        FitnessDatabase db = FitnessDatabase.getDatabase(context);
        
        // 检查是否已经加载过数据
        if (db.fitnessDao().getExerciseCount() > 0) {
            // 只更新MET值和GIF映射，不重复插入数据
            updateCoreExerciseMetValues(db);
            updatePushUpGifMappings(db);
            return; 
        }
        
        // 初始化所有运动数据
        List<Exercise> exercises = new ArrayList<>();
        
        // ========== 上肢运动 - 俯卧撑系列 ==========
        exercises.add(new Exercise("Knee Push-up", "Upper Body", "Push-ups", "Easy", 3.5, "a2", "counter", "", "Chest + Arms + Shoulders", 
            "• Place knees on ground, hands flat on floor\n• Keep your body straight from knees to head\n• Lower until chest almost touches ground\n• Push back up until arms are straight\n• Breathe in going down, breathe out pushing up\n• Start with 8-12 reps"));
        exercises.add(new Exercise("Standard Push-up", "Upper Body", "Push-ups", "Medium", 6.5, "a1", "counter", "", "Chest + Arms + Shoulders", 
            "• Place hands on floor, toes touching ground\n• Keep body straight like a plank from head to feet\n• Tighten your belly, don't let hips sag\n• Lower chest to almost touch floor, then push up\n• Take 2-3 seconds for each rep"));
        exercises.add(new Exercise("Pike Push-up", "Upper Body", "Push-ups", "Hard", 7.5, "pike_push_up", "counter", "", "Shoulders + Arms + Chest", 
            "• Hands on floor, lift hips high like upside-down V\n• Look down between your hands\n• Lower head toward floor, focus on shoulders\n• Keep legs fairly straight\n• Try wall handstands first if shoulders feel weak"));
        
        // ========== 上肢运动 - 臂屈伸系列 ==========
        exercises.add(new Exercise("Ground/Low Bar Dips", "Upper Body", "Dips", "Easy", 3.0, "bench_dips", "counter", "", "Chest + Triceps + Shoulders", 
            "• Sit on edge of bench, hands beside hips\n• Move hips off bench, legs straight or bent\n• Lower body straight down, elbows go back\n• Go down until elbows make 90 degrees\n• Keep shoulders down and back, don't hunch up"));
        exercises.add(new Exercise("Assisted Dips", "Upper Body", "Dips", "Medium", 7.0, "assisted_dips", "counter", "", "Chest + Triceps + Shoulders", 
            "• Use resistance band or machine for help\n• Grip parallel bars, hang with straight arms\n• Lower slowly, keep elbows close to body\n• Go down until shoulders dip below elbows\n• Use less help as you get stronger"));
        exercises.add(new Exercise("Standard Dips", "Upper Body", "Dips", "Hard", 8.0, "dips", "counter", "", "Chest + Triceps + Shoulders", 
            "• Grip bars and hang with full body weight\n• Keep body upright, don't lean too far forward\n• Control the way down, feel chest and triceps stretch\n• Push up focusing on chest and back of arms\n• Skip this if shoulders hurt"));
        
        // ========== 上肢运动 - 引体向上系列 ==========
        exercises.add(new Exercise("Dead Hang", "Upper Body", "Pull-ups", "Easy", 3.0, "deadhang", "timer", "", "Grip + Forearms + Lats", 
            "• Grab bar with palms facing away from you\n• Hang with arms straight, feel shoulders pull down\n• Keep body still, don't swing\n• Focus on grip strength and steady hanging\n• Work up to 30-60 seconds"));
        exercises.add(new Exercise("Assisted Pull-up", "Upper Body", "Pull-ups", "Medium", 8.0, "assisted_pullups", "counter", "", "Lats + Biceps + Shoulders", 
            "• Use resistance band or machine for help\n• Start hanging, pull shoulders down and back\n• Think about pulling elbows to your sides\n• Pull chin over bar, control the way down\n• Feel your back muscles working"));
        exercises.add(new Exercise("Standard Pull-up", "Upper Body", "Pull-ups", "Hard", 9.0, "pullup", "counter", "", "Lats + Biceps + Shoulders", 
            "• Grab bar with hands wider than shoulders\n• Start by pulling shoulders down, then pull up\n• Push chest out, squeeze back muscles tight\n• Pull chin over bar, lower slowly\n• No swinging - keep movement smooth"));
        
        // ========== 下肢运动 - 弓箭步系列（统一MET值为5.0）==========
        exercises.add(new Exercise("Standard Lunge", "Lower Body", "Lunges", "Medium", 5.0, "lunges", "counter", "", "Quads + Glutes + Calves", 
            "• Stand with feet hip-width apart\n• Step one foot forward into a big step\n• Lower until front thigh is flat (parallel to floor)\n• Front knee stays over ankle, back knee near floor\n• Push through front heel to return to start"));
        exercises.add(new Exercise("Weighted Lunge", "Lower Body", "Lunges", "Hard", 5.0, "lunges", "counter", "", "Quads + Glutes + Calves", 
            "• Hold dumbbells or weights in hands\n• Keep chest up and core tight\n• Put most weight on front foot\n• Go down slowly, feel the stretch\n• Choose weight that lets you do perfect form"));
        
        // ========== 下肢运动 - 深蹲系列（统一MET值为5.5）==========
        exercises.add(new Exercise("Standard Squat", "Lower Body", "Squats", "Medium", 5.5, "squats", "counter", "", "Quads + Glutes + Hamstrings", 
            "• Stand with feet shoulder-width apart\n• Point toes slightly outward\n• Sit back with hips, chest up and proud\n• Go down until thighs are flat or lower\n• Push through heels, squeeze butt to stand up"));
        exercises.add(new Exercise("Weighted Squat", "Lower Body", "Squats", "Hard", 5.5, "squats", "counter", "", "Quads + Glutes + Hamstrings", 
            "• Add weight with barbell or dumbbells\n• Keep back straight, don't round forward\n• Knees track same direction as toes\n• Keep weight on heels and middle of feet\n• Breathe in going down, out coming up"));
        
        // ========== 下肢运动 - 保加利亚蹲系列（统一MET值为6.0）==========
        exercises.add(new Exercise("Standard Bulgarian Split Squat", "Lower Body", "Bulgarian Split Squats", "Medium", 6.0, "bogarian", "counter", "", "Quads + Glutes + Balance", 
            "• Put back foot on bench, front foot planted\n• Most weight on front leg, back leg just helps\n• Lower until front thigh is flat\n• Keep chest up, don't lean forward\n• Push through front heel, feel butt squeeze"));
        exercises.add(new Exercise("Weighted Bulgarian Split Squat", "Lower Body", "Bulgarian Split Squats", "Hard", 6.0, "bogarian", "counter", "", "Quads + Glutes + Balance", 
            "• Hold dumbbells or wear weighted vest\n• Practice balance first - use wall if needed\n• Go down slowly for extra challenge\n• Front knee stays over ankle\n• Finish one side completely before switching"));
        
        // ========== 核心运动 - 卷腹系列（统一MET值为4.5，按次数计算）==========
        exercises.add(new Exercise("Crunch", "Core", "Crunches", "Easy", 4.5, "crunch", "counter", "Target: 10 reps", "Upper Abs + Core", 
            "• Lie on back, knees bent, feet flat on floor\n• Place hands lightly behind head, don't pull neck\n• Lift your head and shoulders using belly muscles\n• Breathe out going up, breathe in going down\n• Small movement - just focus on belly squeeze"));
        exercises.add(new Exercise("Crunch", "Core", "Crunches", "Medium", 4.5, "crunch", "counter", "Target: 20 reps", "Upper Abs + Core", 
            "• Keep perfect form but do more reps\n• Pause for 1-2 seconds at the top\n• Don't use momentum - control every rep\n• Feel your abs stay tight the whole time\n• Try different speeds to mix it up"));
        exercises.add(new Exercise("Crunch", "Core", "Crunches", "Hard", 4.5, "crunch", "counter", "Target: 30 reps", "Upper Abs + Core", 
            "• High reps to build belly muscle endurance\n• Keep breathing steady, don't hold breath\n• Control every single rep, no rest\n• Try super slow crunches for extra challenge\n• Your abs should feel a strong burning sensation"));
        
        // ========== 核心运动 - 平板支撑系列（按时间计算，不同难度不同MET值）==========
        exercises.add(new Exercise("Plank", "Core", "Planks", "Easy", 3.5, "plank", "timer", "Target: 30 seconds", "Full Core + Stabilizers", 
            "• Start in push-up position, rest on forearms\n• Body straight like a board from head to feet\n• Squeeze belly tight, don't let hips sag or lift up\n• Breathe normally, don't hold your breath\n• Start with 15 seconds if you're new"));
        exercises.add(new Exercise("Plank", "Core", "Planks", "Medium", 5.0, "plank", "timer", "Target: 1 minute", "Full Core + Stabilizers", 
            "• Keep perfect form for longer time\n• Focus on keeping belly muscles super tight\n• Shoulders directly over elbows\n• Hips level with your back\n• Shaking is normal - just keep holding!"));
        exercises.add(new Exercise("Plank", "Core", "Planks", "Hard", 6.5, "plank", "timer", "Target: 1.5 minutes", "Full Core + Stabilizers", 
            "• Long hold to build serious core strength\n• Keep breathing steady, stay calm\n• Try lifting one arm or one foot for harder version\n• Think about your belly doing all the work\n• After hitting time goal, try for even longer"));
        
        // ========== 核心运动 - 俄罗斯转体系列（统一MET值为4.8，按次数计算）==========
        exercises.add(new Exercise("Russian Twist", "Core", "Russian Twists", "Easy", 4.8, "russian_twists", "counter", "Target: 15 reps", "Obliques + Core", 
            "• Sit with knees bent, heels on ground\n• Lean back 45 degrees, find your balance\n• Hands together, twist side to side\n• Feel the sides of your belly working\n• Keep back straight, don't round forward"));
        exercises.add(new Exercise("Russian Twist", "Core", "Russian Twists", "Medium", 4.8, "russian_twists", "counter", "Target: 25 reps", "Obliques + Core", 
            "• Lift feet off ground to make it harder\n• Twist farther to each side\n• Keep belly tight the entire time\n• Match your breathing with the twisting\n• Each side counts as one complete rep"));
        exercises.add(new Exercise("Russian Twist", "Core", "Russian Twists", "Hard", 4.8, "russian_twists", "counter", "Target: 40 reps", "Obliques + Core", 
            "• Feet completely up for extra challenge\n• Hold weight in hands for more resistance\n• High reps to build side belly endurance\n• Keep quality movement, don't cheat\n• Feel deep burn in side belly muscles"));
        

        // 批量插入所有运动数据
        db.fitnessDao().insertExercises(exercises);
    }
    
    /**
     * 更新核心运动的MET值
     * 确保数据库中的MET值与最新版本一致
     * 
     * @param db 数据库实例
     */
    private static void updateCoreExerciseMetValues(FitnessDatabase db) {

        db.fitnessDao().updateExerciseMetValue("Core", "Crunches", "Easy", 4.5);
        db.fitnessDao().updateExerciseMetValue("Core", "Crunches", "Medium", 4.5);
        db.fitnessDao().updateExerciseMetValue("Core", "Crunches", "Hard", 4.5);
        
        db.fitnessDao().updateExerciseMetValue("Core", "Planks", "Easy", 3.5);
        db.fitnessDao().updateExerciseMetValue("Core", "Planks", "Medium", 5.0);
        db.fitnessDao().updateExerciseMetValue("Core", "Planks", "Hard", 6.5);
         
        db.fitnessDao().updateExerciseMetValue("Core", "Russian Twists", "Easy", 4.8);
        db.fitnessDao().updateExerciseMetValue("Core", "Russian Twists", "Medium", 4.8);
        db.fitnessDao().updateExerciseMetValue("Core", "Russian Twists", "Hard", 4.8);
    }
    
    /**
     * 更新俯卧撑的GIF映射
     * 修复GIF文件名映射问题：Standard Push-up对应a1.gif，Knee Push-up对应a2.gif
     * 
     * @param db 数据库实例
     */
    private static void updatePushUpGifMappings(FitnessDatabase db) {
        // 修复GIF映射：Standard Push-up -> a1, Knee Push-up -> a2
        db.fitnessDao().updateExerciseVideoUrl("Standard Push-up", "Upper Body", "a1");
        db.fitnessDao().updateExerciseVideoUrl("Knee Push-up", "Upper Body", "a2");
    }
}
