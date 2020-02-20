package kr.hs.emirim.mirimhee.quizlocker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder

class LockScreenService : Service() {
    var reciver:ScreenOffReciver? =null

    private val ANDROID_CHANNEL_ID = "com.akj.quizloker"
    private val NOTIFICATION_ID= 9999

    override fun onCreate() {
        super.onCreate()

        if(reciver == null){
            reciver = ScreenOffReciver()
            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(reciver,filter)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        if(intent != null){
            if(intent.action == null){
                if(reciver==null){
                    reciver = ScreenOffReciver()
                    val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
                    registerReceiver(reciver,filter)
                }
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val chan = NotificationChannel(ANDROID_CHANNEL_ID,"MyService",NotificationManager.IMPORTANCE_NONE)
            chan.lightColor=Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            val manager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            manager.createNotificationChannel(chan)

            val builder = Notification.Builder(this,ANDROID_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("SmartTracker Running")
            val notification = builder.build()

            startForeground(NOTIFICATION_ID,notification)
        }
        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        if (reciver != null){
            unregisterReceiver(reciver)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
