package com.weizu.navi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.weizu.navi.R
import com.weizu.navi.entity.LoginResult
import com.weizu.navi.entity.User
import com.weizu.navi.models.UserViewModel

class DetailFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 可能是登录后的回退到这个页面
        Log.e("TAG", "onCreate: 可能是登录后的回退到这个页面", )
        val navController = NavHostFragment.findNavController(this)
        // 检索当前的nav
        val currentBackStackEntry = navController.currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        if (currentBackStackEntry != null) {
            savedStateHandle?.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
                ?.observe(currentBackStackEntry, object : Observer<Boolean>{
                    override fun onChanged(flag: Boolean?) {
                        if(flag == true) {
                            // 登录成功
                            val startDestinationId = navController.graph.startDestinationId
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(startDestinationId, true)
                                .build()
                            navController.navigate(startDestinationId, null, navOptions)
                        } else {
                            // 登录失败，回退到游客页面
                            NavHostFragment.findNavController(this@DetailFragment).popBackStack()
                        }
                    }
                })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 判断用户信息是否为空
        userViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
            .get(UserViewModel::class.java)
        val findNavController = this.findNavController()

        userViewModel.user.observe(viewLifecycleOwner) {
            if (it.name == "游客") {
                findNavController.navigate(R.id.loginFragment)
            }
        }

        // 初始化一个游客身份登录
        if(userViewModel.user.value?.name == null) {
            userViewModel.initUser()
        }
    }

}