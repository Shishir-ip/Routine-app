
package com.shishir.routineplannerpro.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shishir.routineplannerpro.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("ACTIVITY_NAME", intent.getStringExtra("ACTIVITY_NAME"))
        }
        context.startActivity(i)
    }
}
