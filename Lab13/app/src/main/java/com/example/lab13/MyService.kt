package com.example.lab13

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    private var channel: String = ""
    private var thread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 解析 Intent 取得頻道資訊
        channel = intent?.getStringExtra("channel") ?: ""

        // 發送初始廣播
        sendBroadcast(
            when (channel) {
                "music" -> "歡迎來到音樂頻道"
                "new" -> "歡迎來到新聞頻道"
                "sport" -> "歡迎來到體育頻道"
                else -> "頻道錯誤"
            }
        )

        // 停止舊的執行緒
        thread?.takeIf { it.isAlive }?.interrupt()

        // 啟動新的執行緒，模擬延遲廣播
        thread = Thread {
            try {
                Thread.sleep(3000) // 延遲三秒
                sendBroadcast(
                    when (channel) {
                        "music" -> "即將播放本月TOP10音樂"
                        "new" -> "即將為您提供獨家新聞"
                        "sport" -> "即將播報本週NBA賽事"
                        else -> "頻道錯誤"
                    }
                )
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.apply { start() }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    // 發送廣播訊息
    private fun sendBroadcast(message: String) {
        val broadcastIntent = Intent(channel).apply {
            putExtra("msg", message)
        }
        sendBroadcast(broadcastIntent)
    }
}
