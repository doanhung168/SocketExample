import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.hung_nguyen.socketexample.R

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "your_channel_id"
        private const val CHANNEL_NAME = "Your Channel Name"
        private const val CHANNEL_DESCRIPTION = "Your Channel Description"
    }

    fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Call this method to show a notification
    fun showNotification() {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Your Title")
            .setContentText("Your Message")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }
}