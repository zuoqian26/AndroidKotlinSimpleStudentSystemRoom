package com.example.studentroomdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentroomdemo.data.entity.Student
import com.example.studentroomdemo.databinding.ActivityMainBinding
import com.example.studentroomdemo.ui.StudentViewModel
import com.example.studentroomdemo.ui.adapter.StudentAdapter

/**
 * MainActivity 类 - 主界面Activity
 *
 * 什么是Activity？
 * - Activity是Android的"页面"
 * - 相当于一个"屏幕"或"窗口"
 * - 用户看到的就是一个Activity
 *
 * 继承 AppCompatActivity
 * - 这是Android提供的Activity基类
 * - 提供了很多实用的功能
 *
 * 是否必要：绝对必要！这是用户的入口页面
 */
class MainActivity : AppCompatActivity() {

    /**
     * binding - ViewBinding对象
     *
     * 什么是ViewBinding？
     * - 一种方便访问XML布局中控件的技术
     * - 不需要手动调用findViewById
     * - 通过binding.tvName这样的方式访问控件
     * - 类型安全，不容易出错
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * studentViewModel - ViewModel
     *
     * by viewModels() 是Kotlin的委托属性
     * - 自动创建ViewModel实例
     * - 和Activity生命周期绑定
     * - 屏幕旋转时不会重新创建
     */
    private val studentViewModel: StudentViewModel by viewModels()

    /**
     * adapter - RecyclerView适配器
     *
     * 用来显示学生列表
     */
    private val adapter = StudentAdapter()

    /**
     * pickImageLauncher - 图片选择器启动器
     *
     * 什么是ActivityResultLauncher？
     * - 用于启动系统组件并获取结果
     * - 这里用来打开系统相册选择图片
     *
     * registerForActivityResult
     * - 注册一个结果回调
     * - ActivityResultContracts.OpenDocument() 是打开文档（图片）的契约
     *
     * lambda表达式 (uri: Uri?) -> {...}
     * - 这是回调函数
     * - 当用户选择完图片后会调用这个函数
     * - uri就是用户选择的图片的Uri
     */
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        // 如果用户选择了图片（不是取消）
        uri?.let {
            // 关键步骤：持久化Uri权限
            // 如果不调用这个方法，App重启后就无法访问这个图片了
            // 因为Android的安全机制，访问权限有时间限制
            // 通过takePersistableUriPermission，我们获得了永久访问这个图片的权限
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // 添加新学生（演示用，实际项目中会让用户输入姓名年龄等）
            addNewStudent(it.toString())
        }
    }

    /**
     * onCreate - Activity创建时调用
     *
     * 什么是onCreate？
     * - Activity生命周期方法之一
     * - Activity创建时首先调用这个方法
     * - 在这里做界面初始化的工作
     *
     * 参数 savedInstanceState
     * - 如果Activity之前被销毁过，这里会保存之前的状态
     * - 首次创建时是null
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 初始化ViewBinding
        // inflate方法把XML布局转换成View对象
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 2. 设置内容视图
        // 把View设置给Activity，这样用户就能看到了
        setContentView(binding.root)

        // 3. 设置RecyclerView
        // 设置适配器
        binding.recyclerView.adapter = adapter
        // 设置布局管理器（线性布局，垂直方向）
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. 观察数据变化
        // observe方法用来观察数据
        // 第一个参数是LifecycleOwner，这里用this（当前Activity）
        // 第二个参数是观察者lambda，当数据变化时调用
        studentViewModel.allStudents.observe(this) { students ->
            // 更新适配器的数据
            // submitList会自动比较差异，只更新变化的部分
            adapter.submitList(students)

            // 如果没有数据显示提示
            if (students.isEmpty()) {
                Toast.makeText(this, "点击右下角按钮添加学生", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. 设置添加按钮点击事件
        binding.fabAdd.setOnClickListener {
            // 点击按钮，打开相册选择图片
            // launch方法启动图片选择器
            // arrayOf("image/*") 表示只显示图片类型的文件
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }

    /**
     * addNewStudent - 添加新学生
     *
     * 这是一个演示方法
     * 实际项目中，这里应该弹出对话框让用户输入姓名、年龄、成绩
     * 这里为了简化，随机生成一些数据
     *
     * 参数 photoUri: String
     * - 学生照片的Uri路径
     */
    private fun addNewStudent(photoUri: String) {
        // 创建学生对象
        // 随机生成姓名、年龄、成绩
        val newStudent = Student(
            // 用当前时间戳生成一个随机名字
            name = "学生${System.currentTimeMillis() % 1000}",
            // 随机年龄18-25岁
            age = (18..25).random(),
            // 随机成绩60-100分
            score = (60..100).random().toDouble(),
            // 照片Uri
            photoUri = photoUri
        )

        // 调用ViewModel的insert方法插入数据库
        // insert是挂起函数，会在后台线程执行
        studentViewModel.insert(newStudent)

        // 显示提示
        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show()
    }
}
