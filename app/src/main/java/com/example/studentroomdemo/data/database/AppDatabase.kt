package com.example.studentroomdemo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentroomdemo.data.dao.StudentDao
import com.example.studentroomdemo.data.entity.Student

/**
 * AppDatabase 类 - Room数据库的主类
 *
 * 什么是Database？
 * - Database就是"数据库"，它是整个Room的核心
 * - 相当于一个"大管家"，管理所有的表（Entity）和操作（DAO）
 * - 它负责创建数据库文件，并提供访问入口
 *
 * Database的作用：
 * - 创建数据库实例
 * - 关联Entity和DAO
 * - 管理数据库版本
 *
 * 是否必要：绝对必要！没有Database就无法创建和使用数据库
 */

/**
 * @Database 注解
 * - 告诉Room这是一个数据库类
 *
 * entities = [Student::class]
 * - 指定这个数据库包含哪些表（实体类）
 * - 如果有多个表，可以这样写：entities = [Student::class, Teacher::class]
 *
 * version = 1
 * - 数据库的版本号
 * - 当我们需要修改表结构（比如增加一列）时，需要升级版本
 *
 * exportSchema = false
 * - 是否导出数据库schema（表结构）
 * - false表示不导出，一般开发时设为false
 */
@Database(
    entities = [Student::class],
    version = 1,
    exportSchema = false
)
/**
继承 RoomDatabase
 * - 这是一个抽象类，我们需要实现它
 * - 内部包含了数据库的创建、升级等逻辑
 */
abstract class AppDatabase : RoomDatabase() {

    /**
     * 抽象方法：返回StudentDao
     * - 相当于提供一个"入口"
     * - 通过这个入口，我们可以执行DAO中定义的所有操作
     *
     * 为什么是抽象的？
     * - Room会自动生成实现代码
     * - 我们只需要声明这个方法就行
     */
    abstract fun studentDao(): StudentDao

    /**
     * companion object - 伴生对象
     * - 类似于Java中的静态方法
     * - 用来提供全局唯一的数据库实例
     */
    companion object {

        /**
         * @Volatile 注解
         * - 保证这个变量在多线程环境下可见
         * - 防止由于线程缓存导致的空指针问题
         */
        @Volatile

        /**
         * INSTANCE - 单例实例
         * - 整个应用只有一个数据库实例
         * - 节省内存资源
         */
        private var INSTANCE: AppDatabase? = null

        /**
         * getDatabase 方法
         * - 获取数据库实例的静态方法
         * - 如果实例不存在，就创建一个；如果存在，就返回已有的
         *
         * 参数 context: Context
         * - 需要传入上下文
         * - 通常传入Application Context，避免内存泄漏
         *
         * return AppDatabase
         * - 返回数据库实例
         */
        fun getDatabase(context: Context): AppDatabase {
            // 如果实例已经存在，直接返回
            return INSTANCE ?: synchronized(this) {
                // 如果实例不存在，创建新的
                val instance = Room.databaseBuilder(
                    // 上下文
                    context.applicationContext,
                    // 数据库类
                    AppDatabase::class.java,
                    // 数据库文件名
                    // 这个文件会保存在手机的 /data/data/包名/databases/ 目录下
                    "student_database"
                )
                    // 如果需要，可以在这里添加更多配置
                    // 比如 .fallbackToDestructiveMigration() - 破坏性升级
                    .build()

                // 保存实例
                INSTANCE = instance

                // 返回实例
                instance
            }
        }
    }
}
