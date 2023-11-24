package com.example.test_project.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.test_project.Methods;

import static android.content.Context.ALARM_SERVICE;

public class ReminderMethods {

    Context context;
    Methods methods = new Methods();

    public void scheduleReminder(String title, long timestampp, int id, Context con){

        context = con;

        Intent intent = new Intent(context, ReminderReciver.class);
        intent.putExtra("title",title);
        intent.putExtra("time",methods.getTimestampToTime(timestampp/1000));
        intent.putExtra("id",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null){
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timestampp,
                    pendingIntent
            );
        }

    }
}
