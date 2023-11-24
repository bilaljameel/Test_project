package com.example.test_project.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.showNotification(context,"Schedule is : "+intent.getStringExtra("title"),intent.getStringExtra("time"),intent.getIntExtra("id",0));

    }
}
