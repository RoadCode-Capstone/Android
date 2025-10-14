package com.example.roadcode.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (!didUseAppToday(context)) {
            showNotification(context)
        } else { // 오늘 이미 앱을 실행했다면 알림 안 보냄
            Log.d("Alarm", "오늘 이미 앱을 실행했으므로 알림 생략")
        }

        scheduleDailyNotification(context, 18, 0) // 다음날 알림 다시 예약
    }

    /* 오늘 앱 실행 여부 확인 */
    private fun didUseAppToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences("attendance_prefs", Context.MODE_PRIVATE)
        val lastActive = prefs.getString("last_active_date", null)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return lastActive == today
    }
}
