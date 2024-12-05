package com.example.lab13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // 建立廣播接收器
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.extras?.getString("msg")
            findViewById<TextView>(R.id.tvMsg).text = message ?: "No message received"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 適配 WindowInsets，確保 UI 元素不被系統欄遮擋
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 註冊按鈕事件
        setupButtonListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除廣播接收器的註冊
        unregisterReceiver(receiver)
    }

    private fun setupButtonListeners() {
        val buttonMapping = mapOf(
            R.id.btnMusic to "music",
            R.id.btnNew to "new",
            R.id.btnSport to "sport"
        )

        buttonMapping.forEach { (buttonId, channel) ->
            findViewById<Button>(buttonId).setOnClickListener {
                registerChannel(channel)
            }
        }
    }

    private fun registerChannel(channel: String) {
        // 設定接收的頻道
        val intentFilter = IntentFilter(channel)

        // 註冊廣播接收器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }

        // 啟動服務並傳遞頻道資料
        Intent(this, MyService::class.java).also { intent ->
            intent.putExtra("channel", channel)
            startService(intent)
        }
    }
}
