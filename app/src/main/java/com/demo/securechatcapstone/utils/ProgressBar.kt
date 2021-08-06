package com.demo.securechatcapstone.utils

import android.app.Activity
import android.app.AlertDialog
import com.demo.securechatcapstone.R

class ProgressBar(val activity: Activity) {
    private lateinit var dialog: AlertDialog
    fun loading() {
        val builder = AlertDialog.Builder(activity)
        dialog =
            builder.setView(activity.layoutInflater.inflate(R.layout.progress_bar, null)).create()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}