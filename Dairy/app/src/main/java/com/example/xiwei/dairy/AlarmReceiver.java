package com.example.xiwei.dairy;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private Context context;
    private MediaPlayer mp;

    //private NotificationManager mNotificationManager;

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == WriteSchedule.INTENT_ALARM_LOG) {
            NotificationManager manager= (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);//to define a notifacation
            Notification notification =new NotificationCompat.Builder(context,"my_channel_01") //set the basic parameters
                    .setContentTitle("SCHEDULE TIME!")
                    .setContentText("Time to Finish Your Task!")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build();
            manager.notify(1,notification); // show the notification in the system notification bar
        }
    }
}

