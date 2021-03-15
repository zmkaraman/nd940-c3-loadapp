package com.udacity


import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private val REQUEST_CODE = 0
private val FLAGS = 0
private val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var targetURL: String?  = null
    private var downloadStatus: String?  = "Failure"

    lateinit var customizeButton: LoadingButton


    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        customizeButton = findViewById(R.id.custom_button)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )

        custom_button.setOnClickListener {

            if (targetURL == null) {

                val editTextUrl = editTextUrl.text.toString()

                if (editTextUrl.isEmpty() ) {
                    Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT).show()
                } else {
                    targetURL = editTextUrl
                    if (URLUtil.isValidUrl(targetURL)) {
                        customizeButton.startAnimation()
                        download()
                    } else {
                        Toast.makeText(this, "You must enter a valid URL", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {

                customizeButton.startAnimation()
                download()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Toast.makeText(applicationContext, "Download complete!", Toast.LENGTH_SHORT).show()
            downloadStatus = "Success"
            sendNotification()
        }
    }

    private fun sendNotification() {
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager


        val detailIntent = Intent(applicationContext, DetailActivity::class.java)
        detailIntent.putExtra("name", targetURL)
        detailIntent.putExtra("status",downloadStatus)
        val detailPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationManager.sendNotification("Download complete!",
            applicationContext, detailPendingIntent
        )
    }

    private fun download() {

        val notificationManager =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()


        val request =
            DownloadManager.Request(Uri.parse(targetURL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_item_1 ->
                    if (checked) {
                        targetURL = GLIDE_URL
                    }
                R.id.radio_item_2 ->
                    if (checked) {
                        targetURL = UDACITY_URL
                    }
                R.id.radio_item_3 ->
                    if (checked) {
                        targetURL = RETROFIT_URL
                    }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.download_notification_channel_description)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }


    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
    }

}
