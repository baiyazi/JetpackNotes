[toc]
# 1. 前言
在这篇博客中，介绍一下定义数据库表的时候的多种关系。参考文档地址：[定义对象之间的关系](https://developer.android.google.cn/training/data-storage/room/relationships?hl=zh_cn)
# 2. 多表关系
## 2.1 基本嵌套
在`OOP`的思想中，万物均对象。故而在创建用户类的时候，下面的写法并不陌生：
~~~kotlin
data class Address(
    val street: String?,
    val state: String?,
    val city: String?,
    @ColumnInfo(name = "post_code") val postCode: Int
)

@Entity
data class User(
    @PrimaryKey val id: Int,
    val firstName: String?,
    @Embedded val address: Address?
)
~~~
注意在嵌套的时候，所使用的注解为`@Embedded`。执行完毕后，可以得到包含`id`、`firstName`、`street`、`state`、`city`和`post_code`的`User`表结构。
## 2.2 定义嵌套一对一关系
比如定义了两个表，为`User`和`Library`类。如下：
~~~kotlin
@Entity
data class User(
    @PrimaryKey val userId: Long,
    val name: String,
    val age: Int
)

@Entity
data class Library(
    @PrimaryKey val libraryId: Long,
    val userOwnerId: Long
)
~~~
注意到，在`Library`类中使用了`userOwnerId`来表示用户，对应`User`表中的`userId`字段，这里可以认为为外键关系，为了表示这种关系，可以再定义一个类，用`@Relation`注解来标识这种一对一关系。如下：
~~~kotlin
data class UserAndLibrary(
    @Embedded val user: User,
    @Relation(
         parentColumn = "userId",
         entityColumn = "userOwnerId"
    )
    val library: Library
)
~~~
将`@Relation`注释添加到子实体的实例，同时将`parentColumn`设置为父实体主键列的名称，并将`entityColumn`设置为引用父实体主键的子实体列的名称。最后，向 DAO 类添加一个方法，用于返回将父实体与子实体配对的数据类的所有实例。该方法需要 `Room `运行两次查询，因此应向该方法添加`@Transaction`注释，以确保整个操作以原子方式执行。
~~~kotlin
@Transaction
@Query("SELECT * FROM User")
fun getUsersAndLibraries(): List<UserAndLibrary>
~~~

# 3. 案例
## 3.1 一对一关系


