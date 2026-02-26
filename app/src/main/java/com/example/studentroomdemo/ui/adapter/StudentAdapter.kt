package com.example.studentroomdemo.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentroomdemo.data.entity.Student
import com.example.studentroomdemo.databinding.ItemStudentBinding

/**
 * StudentAdapter 类 - RecyclerView适配器
 *
 * 什么是Adapter？
 * - Adapter是"适配器"的意思
 * - 它是RecyclerView和数据之间的"桥梁"
 * - 负责把数据转换成UI可以显示的View
 *
 * Adapter的作用：
 * - 创建每个item的布局（ViewHolder）
 * - 把数据绑定到每个item上
 * - 管理RecyclerView的显示
 *
 * 使用ListAdapter
 * - 这是RecyclerView.Adapter的增强版
 * - 支持DiffUtil，可以高效地更新数据
 * - 当数据变化时，只更新变化的部分，不用刷新整个列表
 *
 * 是否必要：绝对必要！没有Adapter就无法显示列表
 */

/**
 * StudentAdapter 继承 ListAdapter
 *
 * 第一个参数 Student 是数据类型
 * 第二个参数 StudentViewHolder 是ViewHolder类型
 */
class StudentAdapter : ListAdapter<Student, StudentAdapter.StudentViewHolder>(StudentDiffCallback()) {

    /**
     * StudentViewHolder - ViewHolder（视图持有者）
     *
     * 什么是ViewHolder？
     * - 相当于每个列表项的"容器"
     * - 持有item布局中的所有控件引用
     * - 这样就不用每次都去查找控件，提高性能
     *
     * 继承 RecyclerView.ViewHolder
     * - 需要传入item的根布局
     * - 这里用ViewBinding来访问布局中的控件
     */
    class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * onCreateViewHolder - 创建ViewHolder
     *
     * 什么时候调用？
     * - 当RecyclerView需要显示新的item时调用
     * - 只在刚开始创建几个item时调用，创建后会复用
     *
     * 参数说明：
     * - parent: ViewGroup - 父容器（RecyclerView）
     * - viewType: Int - 视图类型（暂时不用）
     *
     * 返回：
     * - StudentViewHolder - 创建好的ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // 1. 获取LayoutInflater（布局填充器）
        // 用于把XML布局文件转换成View对象
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // 2. 创建ViewHolder并返回
        return StudentViewHolder(binding)
    }

    /**
     * onBindViewHolder - 绑定数据到ViewHolder
     *
     * 什么时候调用？
     * - 当RecyclerView需要显示某个item时调用
     * - 每次滚动出新的item都会调用
     *
     * 参数说明：
     * - holder: StudentViewHolder - 要绑定数据的ViewHolder
     * - position: Int - 当前item的位置
     */
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        // 1. 获取当前位置的学生数据
        val current = getItem(position)

        // 2. 绑定数据到UI
        // 设置姓名
        holder.binding.tvName.text = current.name

        // 设置详细信息（年龄和成绩）
        holder.binding.tvDetails.text = "年龄: ${current.age} | 成绩: ${current.score}"

        // 3. 加载图片（使用Glide）
        /**
         * Glide 是什么？
         * - 是一个图片加载库
         * - 可以从网络、文件系统、Uri等加载图片
         * - 自动处理图片缓存、压缩、裁剪等
         *
         * .with(holder.itemView.context)
         * - 传入上下文
         *
         * .load(Uri.parse(current.photoUri))
         * - 从Uri加载图片
         * - photoUri是字符串，需要转换成Uri对象
         *
         * .placeholder(android.R.drawable.ic_menu_gallery)
         * - 设置占位图
         * - 图片加载过程中显示这个图片
         *
         * .circleCrop()
         * - 把图片裁剪成圆形
         * - 这样学生头像就是圆形的，更好看
         *
         * .into(holder.binding.ivPhoto)
         * - 把加载好的图片设置到ImageView中
         */
        Glide.with(holder.itemView.context)
            .load(Uri.parse(current.photoUri))
            .placeholder(android.R.drawable.ic_menu_gallery)
            .circleCrop()
            .into(holder.binding.ivPhoto)
    }

    /**
     * getItemCount - 获取item数量
     *
     * 返回列表中有多少个item
     */
    override fun getItemCount(): Int {
        return currentList.size
    }

    /**
     * StudentDiffCallback - DiffUtil回调
     *
     * 什么是DiffUtil？
     * - 是一个工具类
     * - 用来比较新旧数据列表的差异
     * - 只更新有变化的部分，提高性能
     */
    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        /**
         * areItemsTheSame - 判断是否是同一个item
         *
         * 判断依据：通常用唯一标识（如ID）来判断
         */
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * areContentsTheSame - 判断内容是否相同
         *
         * 判断依据：比较所有属性是否都相同
         */
        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }
    }
}
