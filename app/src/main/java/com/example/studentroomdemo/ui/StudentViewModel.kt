package com.example.studentroomdemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.studentroomdemo.data.database.AppDatabase
import com.example.studentroomdemo.data.entity.Student
import com.example.studentroomdemo.data.repository.StudentRepository
import kotlinx.coroutines.launch

/**
 * StudentViewModel 类 - ViewModel（视图模型）
 *
 * 什么是ViewModel？
 * - ViewModel是MVVM架构中的"桥梁"
 * - 它负责管理UI需要显示的数据
 * - 它在Activity/Fragment和Repository之间传递数据
 *
 * ViewModel的特点：
 * - 生命周期比Activity长，不会因为屏幕旋转而销毁
 * - 数据可以持久保存
 * - 把UI逻辑和业务逻辑分离，代码更清晰
 *
 * 继承 AndroidViewModel
 * - 这是Android专用的ViewModel
 - 需要传入ApplicationContext
 * - 这样可以在ViewModel中使用上下文
 *
 * 是否必要：
 * - 强烈建议使用！这是Android开发的最佳实践
 * - 如果不用ViewModel，数据操作会混乱，代码难以维护
 */
class StudentViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * repository - 数据仓库
     * - 持有Repository的引用
     * - 通过它来访问数据库
     */
    private val repository: StudentRepository

    /**
     * allStudents - 所有学生数据（LiveData类型）
     *
     * LiveData是什么？
     * - LiveData是一种"可观察"的数据容器
     * - 类似于Observer模式
     * - 当数据变化时，会自动通知观察者（Activity/Fragment）
     *
     * 为什么用LiveData而不是直接用Flow？
     * - LiveData是Android专有的，和UI生命周期绑定
     * - 当Activity销毁时，LiveData会自动停止观察，防止内存泄漏
     * - Flow更通用，适合在非Android环境使用
     *
     * : LiveData<List<Student>>
     * - 声明这是一个包含学生列表的LiveData
     */
    val allStudents: LiveData<List<Student>>

    /**
     * init 初始化块
     * - 当ViewModel创建时，会自动执行这个代码块
     * - 这里完成数据库和Repository的初始化
     */
    init {
        // 1. 获取数据库实例
        // AppDatabase.getDatabase(application) 是获取单例数据库
        // .studentDao() 是获取DAO对象
        val dao = AppDatabase.getDatabase(application).studentDao()

        // 2. 创建Repository
        repository = StudentRepository(dao)

        // 3. 获取所有学生数据
        // repository.allStudents 是Flow类型
        // .asLiveData() 是把Flow转换成LiveData
        // 这样就可以在Activity中观察数据变化了
        allStudents = repository.allStudents.asLiveData()
    }

    /**
     * insert 方法 - 插入学生
     *
     * 为什么要用viewModelScope.launch？
     * - 这是一个协程作用域
     * - 用来启动一个协程，在后台执行耗时操作
     * - 这样就不会卡住主界面
     *
     * 为什么insert要在协程中调用？
     * - 数据库操作是耗时操作
     * - 如果在主线程执行，会导致界面卡顿
     * - 必须放到后台线程（协程）执行
     *
     * 参数 student: Student
     * - 要插入的学生对象
     */
    fun insert(student: Student) = viewModelScope.launch {
        // 调用Repository的insert方法
        // 具体的数据操作由Repository完成
        repository.insert(student)
    }

    /**
     * deleteAll 方法 - 删除所有学生（可选扩展）
     */
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllStudents()
    }
}
