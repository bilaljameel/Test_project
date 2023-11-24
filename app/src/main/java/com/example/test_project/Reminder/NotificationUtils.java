package com.example.test_project.Reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.test_project.MainActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    public static void showNotification(Context context,String title,String message,int id){

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel_id")
                .setSmallIcon(androidx.viewpager.R.drawable.notification_template_icon_bg)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                ;

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id,builder.build());

        createNotificationChannel(context);

    }

    public static void createNotificationChannel(Context context){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
//
//        }

        CharSequence name = "My Notification Channel";
        String description = "Channel for My Notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("channel_id",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null){
            notificationManager.createNotificationChannel(channel);
        }
    }
}
