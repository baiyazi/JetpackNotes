package com.weizu.deeplink.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.weizu.deeplink.R

class BFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.b_fragment_textview).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_BFragment_to_CFragment)
        }

        view.findViewById<Button>(R.id.b_fragment_button).setOnClickListener {
            // 应用内使用DeepLink。
            val request = NavDeepLinkRequest.Builder
                .fromUri("https://github.com/baiyazi/".toUri())
                .build()
            findNavController().navigate(request)
        }
    }
}