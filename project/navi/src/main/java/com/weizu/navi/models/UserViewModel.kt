package com.weizu.navi.models

import android.util.Log
import android.util.MutableDouble
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weizu.navi.entity.LoginResult
import com.weizu.navi.entity.User

/**
 * @author 梦否 on 2022/4/29
 * @blog https://mengfou.blog.csdn.net/
 */
class UserViewModel : ViewModel() {
    var user = MutableLiveData<User>()
    var loginResult = MutableLiveData<LoginResult>()

    fun login(username: String, password: String){
        val flag = (username == "Name" && password == "123")
        if(flag) { // 相等在写入
            user.value = User(username, password)
        }
        loginResult.value = LoginResult(flag)
        Log.e("TAG", "login: $flag")
    }

    fun initUser(){
        user.value = User("游客", "123")
    }
}