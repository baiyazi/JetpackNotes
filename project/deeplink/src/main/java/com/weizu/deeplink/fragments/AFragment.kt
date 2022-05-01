package com.weizu.deeplink.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.weizu.deeplink.MainActivity
import com.weizu.deeplink.R

/**
 * @author 梦否
 * @功能：显式深层链接案例
 * @date 2022年5月1日
 */
class AFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.a_fargment_textview).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_AFragment_to_BFragment)
        }
        // 使用参数
        if(arguments != null) {
            Log.e("TAG", "onViewCreated: ${ requireArguments().getString("Key") }", )
        }

        view.findViewById<Button>(R.id.a_fragment_button).setOnClickListener {
            // 使用显式深层链接
            useExplicitDeepLink()
        }
    }

    private var notificationId = 0

    private fun useExplicitDeepLink(){
        // 设置PendingIntent
        val pendingIntent: PendingIntent = NavDeepLinkBuilder(requireContext())
            .addDestination(R.id.CFragment, Bundle().apply {
                putString("Key", "Value")
            })
            .setGraph(R.navigation.nav_graph)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()
        // 创建通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建一个通知渠道
            val notificationChannel = NotificationChannel(
                activity?.packageName,
                "MyChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "显式深层链接测试"
            val notificationManager: NotificationManager? = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        // 创建Notification
        val notification = NotificationCompat.Builder(
            requireActivity(),
            requireActivity().packageName)
            .setContentTitle("DeepLink")
            .setContentText("深层链接测试")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManagerCompat = NotificationManagerCompat.from(requireActivity())
        notificationManagerCompat.notify(notificationId++, notification)
    }
}