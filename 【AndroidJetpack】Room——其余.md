[toc]
# 1. 前言
在前面两篇博客中介绍了`Room`的一些用法，简单了解了`Room`的使用。在这篇博客中，将继续对`Room`进行介绍。

# 2. Room总结
参考文档：[Room](https://developer.android.google.cn/training/data-storage/room?hl=zh_cn#kts)
- `Room`是在`SQLite`上的一个层封装；
- 简化了传统使用`SQLiteOpenHelper`方式的复杂性，注解操作更加便捷；
- 数据库迁移更加方便，且能够记录每个版本的数据库表构成（`json`文件）；

其内三个最基础的组件为`@Entity`、`@Dao`、`@Database`，分别对应表、操作接口和数据库对象（抽象类）。这里不再重复介绍，可以查看博客：[【Android Jetpack】Room](https://mengfou.blog.csdn.net/article/details/124305084)

对于数据库实例化对象，需要遵循单例模式，在`kotlin`中需要使用`@JvmStatic`来进行标识，且对于每个表（即`@Entity`标识的类）的`Dao`接口需要申明，不需要实现。具体的实现由`Room`框架完成，也就是数据库实例化对象的类文件也为抽象类。对应的核心创建方法为：
~~~kotlin
val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
~~~
## 2.1 复合主键
即使用多个列的组合对实体实例进行唯一标识，比如下面的案例：
~~~kotlin
@Entity(primaryKeys = ["firstName", "lastName"])
data class User(
    val firstName: String?,
    val lastName: String?
)
~~~
## 2.2 忽略字段
在[【Android Jetpack】Room](https://mengfou.blog.csdn.net/article/details/124305084)一文中也提到过，可以使用`@Ignore`来忽略一些字段。

## 2.3 索引
我们知道在数据库表中，可以将数据库中的某些列编入索引，以加快查询速度。故而在创建表的时候，可以指定索引。比如下面的案例：
~~~kotlin
@Entity(indices = [Index(value = ["last_name", "address"])])
data class User(
    val address: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @Ignore val picture: Bitmap?
)
~~~

## 2.4 SQL传递参数
### 2.4.1 简单参数
其实在前面的文章中我们都使用过，这里再次回顾一下，比如下面的案例：
~~~kotlin
@Query("SELECT * FROM user WHERE age > :minAge")
fun loadAllUsersOlderThan(minAge: Int): Array<User>
~~~
在注解`@Query`的参数中，这里传递参数使用`:`，比如`:minAge`。

### 2.4.2 集合参数
当然，有些时候需要使用集合类型的参数，比如下面的案例：
~~~kotlin
@Query("SELECT * FROM user WHERE region IN (:regions)")
fun loadUsersFromRegions(regions: List<String>): List<User>
~~~

## 2.5 多表查询
### 2.5.1 普通条件查询
其实也就是`SQL`语句中的多表查询，比如：
~~~kotlin
@Query(
    "SELECT * FROM book " +
    "INNER JOIN loan ON loan.book_id = book.id " +
    "INNER JOIN user ON user.id = loan.user_id " +
    "WHERE user.name LIKE :userName"
)
fun findBooksBorrowedByNameSync(userName: String): List<Book>
~~~
### 2.5.2 联合查询
比如下面的案例，在`SQL`语句中返回的是`user`表和`book`的笛卡尔积表结果：
~~~kotlin
@Query(
    "SELECT * FROM user" +
    "JOIN book ON user.id = book.user_id"
)
fun loadUserAndBookNames(): Map<User, List<Book>>
~~~
返回值类型也值得注意，为`Map<User, List<Book>>`。当然还可以使用条件过滤，比如：
~~~kotlin
@Query(
    "SELECT * FROM user" +
    "JOIN book ON user.id = book.user_id" +
    "GROUP BY user.name WHERE COUNT(book.id) >= 3"
)
fun loadUserAndBookNames(): Map<User, List<Book>>
~~~

