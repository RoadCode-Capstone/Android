package com.example.roadcode.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.roadcode.MainActivity
import com.example.roadcode.R
import java.util.Calendar

/* 알림 출력 */
fun showNotification(context: Context) {
    // 알림 채널 설정 (Android 8.0 이상)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Your Channel Description"
        }
        val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // 알림 생성
    val builder = NotificationCompat.Builder(context, "your_channel_id")
        .setSmallIcon(R.drawable.mascot)
        .setContentTitle("오늘 출석하지 않았어요")
        .setContentText("앱에 접속해서 포인트를 받아보세요 \uD83C\uDF81")
        .setContentIntent(pendingIntent)   // 알림 눌렀을 때 MainActivity 실행
        .setAutoCancel(true)               // 알림 터치 시 자동 삭제
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // 알림 표시
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 🔸 권한이 없으면 알림을 표시하지 않음
            return
        }

        notify(1, builder.build())
    }
}

/* 알림 예약 */
fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1) // 이미 지난 시간이라면 다음날로
    }

    // 매일 같은 시간에 반복
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}
