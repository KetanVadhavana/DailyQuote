package com.example.dailyquote.domain.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dailyquote.QuoteApp.Companion.CHANNEL_ID
import com.example.dailyquote.R
import com.example.dailyquote.data.remote.util.Resource
import com.example.dailyquote.domain.model.Quote
import com.example.dailyquote.domain.repository.QuoteRepository
import com.example.dailyquote.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncDailyQuoteWorker @AssistedInject constructor(
    private val quoteRepository: QuoteRepository,
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        var success = true

        quoteRepository.getRandomQuote().collect { response ->
            if (response is Resource.Success) {
                response.data?.let { sendNotification(it) }
                return@collect
            } else if (response is Resource.Error) {
                success = false
                return@collect
            }
        }

        return if (success) Result.success() else Result.retry()

    }

    private fun sendNotification(quote: Quote) {

        with(NotificationManagerCompat.from(context)) {

            Timber.tag("DailyQuote").d("Quote of the Day : ${quote.quote}")

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


}