package com.udacity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DetailReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, DetailActivity::class.java)

        i.putExtra("name", intent.getStringExtra("name"))
        i.putExtra("status",intent.getStringExtra("status"))
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)

    }

}