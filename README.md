# Fitness Workout App

CS306 期末项目：Android 健身训练应用。按身体部位浏览运动、查看动作演示与要点，支持训练计时/计数与历史记录。

## 功能概览

- **首页**：上肢 / 下肢 / 核心 三大入口，显示身高体重，支持进入历史记录
- **首次启动**：引导填写身高、体重并保存
- **子分类**：按部位展示子分类（如 Push-ups、Squats 等）及运动数量
- **运动列表**：展示该分类下的运动卡片
- **运动详情**：GIF 演示、目标肌肉、MET 值、难度、训练要点；支持计数器/计时器输入，部分运动支持辅助重量/负重
- **训练流程**：将运动加入当次训练，完成后查看总结（时长、卡路里等）
- **历史记录**：查看过往训练记录

## 技术栈

- **语言**：Java 17
- **最低 SDK**：24，**目标 SDK**：34
- **架构**：Activity + ViewBinding，Room 本地数据库
- **主要依赖**：AndroidX AppCompat、Material、Room、RecyclerView、Glide（GIF）、Gson

## 项目结构（简要）

```
app/src/main/java/com/example/finalproject/
├── MainActivity.java           # 主界面
├── ProfileSetupActivity.java   # 首次启动资料设置
├── SubCategoryActivity.java    # 子分类列表
├── ExerciseListActivity.java   # 运动列表
├── ExerciseDetailActivity.java # 运动详情（GIF、计时/计数）
├── WorkoutSummaryActivity.java # 训练总结
├── HistoryActivity.java        # 历史记录
├── adapter/                    # RecyclerView 适配器
├── data/                       # Room 实体与数据库（Exercise, WorkoutRecord, FitnessDatabase, FitnessDao）
└── utils/                      # 工具类（ExerciseDataLoader, UserProfileManager, WorkoutSession, JsonHelper）
```

## 如何运行

1. 用 Android Studio 打开项目根目录（含 `build.gradle.kts` 的目录）
2. 等待 Gradle 同步完成
3. 连接设备或启动模拟器，点击 Run 运行 `app`

或命令行构建 Debug APK：

```bash
./gradlew assembleDebug
```

APK 输出路径：`app/build/outputs/apk/debug/app-debug.apk`

## 许可证

课程项目，仅供学习使用。
