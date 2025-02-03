package com.example.dailyquote.domain.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dailyquote.QuoteApp.Companion.CHANNEL_ID
import com.example.dailyquote.R
import com.example.dailyquote.domain.model.Quote
import com.example.dailyquote.ui.MainActivity

class NotificationWorker(
    private val context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val quoteString = params.inputData.getString("quote")

        quoteString?.let {

            val quote = Quote.fromJson(quoteString)

            with(NotificationManagerCompat.from(context)) {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@with
                }

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                    context, 0,
                    intent, PendingIntent.FLAG_IMMUTABLE
                )

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(quote.author)
                    .setContentText(">" + quote.quote)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(">>" + quote.quote)
                    )
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // notificationId is a unique int for each notification that you must define.
                notify(0, builder.build())
            }
        }

        return Result.success()

    }


}