package com.example.studentroomdemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.studentroomdemo.data.entity.Student
import kotlinx.coroutines.flow.Flow

/**
 * StudentDao 接口 - 数据访问对象
 *
 * 什么是DAO？
 * - DAO = Data Access Object（数据访问对象）
 * - 就像一个"服务员"，帮我们和数据库"点菜"（查询数据）和"下单"（插入数据）
 * - 我们不需要自己写SQL语句，Room会自动帮我们实现这些方法
 *
 * DAO的作用：
 * - 定义对数据库的操作（增删改查）
 * - 把SQL语句封装成简单的方法调用
 *
 * 是否必要：绝对必要！没有DAO就无法操作数据库
 */

/**
 * @Dao 注解
 * - 告诉Room这是一个DAO接口
 * - Room会根据这个接口自动生成实现代码
 */
@Dao
interface StudentDao {

    /**
     * 插入一个学生
     *
     * @Insert 注解
     * - 告诉Room这是一个插入操作
     * - Room会自动生成INSERT INTO ... 的SQL语句
     *
     * suspend 关键字
     * - 表示这是一个"挂起函数"
     * - 意思是这个函数可以在后台线程执行，不会卡住主界面
     * - 因为数据库操作比较慢，必须在后台进行
     * - 调用这个函数时，需要在协程（coroutine）中调用
     *
     * 参数 student: Student
     * - 要插入的学生对象
     * - Room会自动把对象的属性映射到表的列
     */
    @Insert
    suspend fun insert(student: Student)

    /**
     * 查询所有学生
     *
     * @Query 注解
     * - 自定义SQL查询
     * - 里面的SQL语句会直接执行
     *
     * return Flow<List<Student>>
     * - Flow是"流"的意思，这是一种响应式数据更新方式
     * - 当数据库发生变化时，Flow会自动通知观察者
     * - 这样我们就不用手动刷新列表了，UI会自动更新
     *
     * SELECT * FROM student_table
     * - 查询student_table表中的所有数据
     * - * 表示查询所有列
     *
     * ORDER BY id DESC
     * - 按id倒序排列
     * - DESC表示降序（大的在前面），所以最新的学生会在最上面
     */
    @Query("SELECT * FROM student_table ORDER BY id DESC")
    fun getAllStudents(): Flow<List<Student>>

    /**
     * 根据ID查询一个学生（可选的扩展功能）
     *
     * :id 表示这是方法的参数，会替换到SQL中
     */
    @Query("SELECT * FROM student_table WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?

    /**
     * 删除所有学生（可选的扩展功能）
     *
     * @Query 也可以用于删除操作
     */
    @Query("DELETE FROM student_table")
    suspend fun deleteAllStudents()
}
