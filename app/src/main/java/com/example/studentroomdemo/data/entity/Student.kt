package com.example.studentroomdemo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Student 实体类 - 这是数据库的表结构定义
 *
 * 什么是Entity？
 * - Entity就是"实体类"，它代表了数据库中的一张表
 * - 就像我们生活中的"学生"一样，有姓名、年龄、成绩等属性
 * - 在Room中，我们用Kotlin类来描述这个"学生"的结构
 *
 * 这个类的作用：
 * - 告诉Room数据库，这张表有哪些列（字段）
 * - 每个字段叫什么名字，是什么类型
 *
 * 是否必要：绝对必要！没有Entity就无法创建表
 */

/**
 * @Entity 注解
 * - 告诉Room这是一个数据库表的定义
 * - tableName = "student_table" 给表起个名字
 * - 不写tableName的话，默认用类名作为表名（Student）
 */
@Entity(tableName = "student_table")
data class Student(

    /**
     * @PrimaryKey 注解
     * - 表示这是主键，主键是唯一标识每一行数据的
     * - 就像学生的学号，每个学生都有一个唯一的学号
     *
     * autoGenerate = true
     * - 表示自动生成主键的值（1, 2, 3...）
     * - 我们不需要自己手动设置id，Room会自动帮我们生成
     *
     * val id: Int = 0
     * - Int类型，默认值是0
     * - 当我们插入数据时，如果不传id，就会自动生成
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * val name: String
     * - 学生的姓名，字符串类型
     * - 没有注解的字段，默认就是表的普通列
     */
    val name: String,

    /**
     * val age: Int
     * - 学生的年龄，整数类型
     */
    val age: Int,

    /**
     * val score: Double
     * - 学生的成绩，浮点数类型（可以存小数，比如95.5）
     */
    val score: Double,

    /**
     * val photoUri: String
     * - 学生照片的路径，字符串类型
     *
     * 为什么存String而不是存图片本身？
     * - 如果把图片直接存到数据库（存成BLOB类型），会让数据库变得很大
     * - 读取速度也会变慢
     * - 所以我们只存照片的文件路径（Uri），然后用Glide根据路径加载图片
     * - 这样数据库小，速度快
     */
    val photoUri: String

)
