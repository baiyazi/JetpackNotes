package com.weizu.navi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.weizu.navi.R
import com.weizu.navi.entity.LoginResult
import com.weizu.navi.models.UserViewModel


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private lateinit var userViewModel: UserViewModel
    private var savedStateHandle: SavedStateHandle? = null

    companion object {
        const val LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
            .get(UserViewModel::class.java)
        // 检索上一个目的地的NavBackStackEntry
        savedStateHandle = Navigation.findNavController(view)
            .previousBackStackEntry
            ?.savedStateHandle
        // 初始时刻，设置登录状态为false
        savedStateHandle?.set(LOGIN_SUCCESSFUL, false)

        // 获取用户输入的数据
        val userName = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val password = view.findViewById<EditText>(R.id.editTextTextPassword)
        val submit = view.findViewById<Button>(R.id.submit)

        submit.setOnClickListener {
            login(userName.text.toString(), password.text.toString())
        }

        userViewModel.loginResult.observe(viewLifecycleOwner) {
            Log.e("TAG", "onViewCreated: 哈哈哈 ${ it?.success }" )
            if (it?.success == true) {
                savedStateHandle?.set(LOGIN_SUCCESSFUL, true);
                NavHostFragment.findNavController(this@LoginFragment).popBackStack()
                Snackbar.make(view, "登录成功", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                Snackbar.make(view, "登录失败", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun login(userName: String, passWord: String){
        userViewModel.login(userName, passWord)
    }
}