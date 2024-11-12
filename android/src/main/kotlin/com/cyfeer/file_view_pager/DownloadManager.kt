package com.cyfeer.file_view_pager

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import io.flutter.plugin.common.MethodChannel

class DownloadManager(var context: Context) {

    private var lastDownloadId = 0L
    var resultMethodChannel: MethodChannel.Result? = null

    public fun registerBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                onDownloadComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED
            )
        } else {
            context.registerReceiver(
                onDownloadComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            )
        }
    }

    public fun unregisterBroadcast() {
        context.unregisterReceiver(onDownloadComplete)
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == lastDownloadId) {
                resultMethodChannel?.success(true)
            }
        }
    }

    public fun downloadFile(url: String) {
        val fileName = url.getFileName()
        var request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request =
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        lastDownloadId = downloadManager.enqueue(request)
    }

    private fun String.getFileName(): String {
        if (this.isEmpty()) return ""
        val strArray: Array<String> = this.split("/".toRegex()).toTypedArray()
        return strArray[strArray.size - 1]
    }
}