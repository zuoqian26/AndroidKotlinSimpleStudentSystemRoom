package com.example.studentroomdemo.data.repository

import com.example.studentroomdemo.data.dao.StudentDao
import com.example.studentroomdemo.data.entity.Student
import kotlinx.coroutines.flow.Flow

/**
 * StudentRepository 类 - 数据仓库
 *
 * 什么是Repository？
 * - Repository可以翻译为"仓库"或"资料馆"
 * - 它是MVVM架构中的一个重要角色
 * - 相当于在ViewModel和Database之间加了一个"中间人"
 *
 * Repository的作用：
 * - 封装数据源（可以切换不同的数据来源，比如网络或本地数据库）
 * - 提供干净、易用的API给ViewModel使用
 * - 让代码更清晰，职责更分明
 *
 * 是否必要：
 * - 这是最佳实践，建议使用
 * - 如果不使用Repository，也可以直接在ViewModel中调用DAO
 * - 但使用Repository是更好的编程习惯
 */
class StudentRepository(private val studentDao: StudentDao) {

    /**
     * allStudents - 所有学生数据
     *
     * 直接从DAO获取Flow类型的列表
     * Flow会自动感知数据库的变化
     * 当有新数据插入时，会自动推送给观察者
     *
     * 为什么是Flow而不是List？
     * - List是静态的，一次性获取数据
     * - Flow是动态的，会持续监听数据变化
     * - 这样我们就不需要手动刷新数据了
     */
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    /**
     * insert 方法 - 插入学生
     *
     * 这是一个"委托"方法
     * 实际上是调用DAO的insert方法
     * 我们把简单地把任务交给DAO去做
     *
     * suspend 关键字
     * - 表示这是一个挂起函数
     * - 需要在协程中调用
     * - 因为数据库操作是耗时的，不能在主线程执行
     *
     * 参数 student: Student
     * - 要插入的学生对象
     */
    suspend fun insert(student: Student) {
        studentDao.insert(student)
    }

    /**
     * getStudentById 方法 - 根据ID查询学生（可选扩展）
     *
     * 这也是委托方法
     * 封装了对DAO的调用
     */
    suspend fun getStudentById(id: Int): Student? {
        return studentDao.getStudentById(id)
    }

    /**
     * deleteAllStudents 方法 - 删除所有学生（可选扩展）
     */
    suspend fun deleteAllStudents() {
        studentDao.deleteAllStudents()
    }
}
