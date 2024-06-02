package com.example.myapplication;


import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ReminderBroadcast extends BroadcastReceiver {
    NotificationManager notificationManager;
    public int a;
    private  ArrayList <ParcelableActivity>[] list = new ArrayList[7];

    @Override
    public void onReceive(Context context, Intent intent) {

        for (int i = 0; i < 7; i++) {
            list[i] = new ArrayList<>();
        }

        for (int i = 0; i < 7; i++) {
            list[i] = intent.getParcelableArrayListExtra("getList" + i);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("u");
        int currDayOfWeek = Integer.parseInt(dateFormat.format(new Date())) - 1;

        int nearestDayOfWeekAct = 0;
        ParcelableActivity nearestActivity = new ParcelableActivity();

        boolean flag = false;
        for (int i = currDayOfWeek; i < 7 || !flag; i++) {
            for (int j = 0; j < list[i].size(); j++) {
                if (i == currDayOfWeek) {
                    //проверка - прошло ли его время
                }
                nearestActivity = list[i].get(j);
                nearestDayOfWeekAct = i;
                flag = true;
            }
        }

        for (int i = 0; i < currDayOfWeek || !flag; i++) {
            for (int j = 0; j < list[i].size(); j++) {
                nearestActivity = list[i].get(j);
                nearestDayOfWeekAct = i;
                flag = true;
            }
        }

        if (flag) {

            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Notification.Builder builder = new Notification.Builder(context, "notifyReminder")
                        .setSmallIcon(R.drawable.baseline_accessible_forward_24)
                        .setContentTitle("TITEL")
                        .setContentText("TEXTTEXTTEXTTEXT")
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setPriority(Notification.PRIORITY_HIGH);

                notificationManager.notify(200, builder.build());


                switch (nearestDayOfWeekAct) {
                    case(0): nearestDayOfWeekAct = 2; break;
                    case(1): nearestDayOfWeekAct = 3; break;
                    case(2): nearestDayOfWeekAct = 4; break;
                    case(3): nearestDayOfWeekAct = 5; break;
                    case(4): nearestDayOfWeekAct = 6; break;
                    case(5): nearestDayOfWeekAct = 7; break;
                    case(6): nearestDayOfWeekAct = 1; break;
                }

                Calendar notifTime = Calendar.getInstance();
                notifTime.set(Calendar.HOUR_OF_DAY, nearestActivity.hours);
                notifTime.set(Calendar.MINUTE, nearestActivity.minuts);
                notifTime.set(Calendar.DAY_OF_WEEK, nearestDayOfWeekAct);

                Intent intent1 = new Intent(context, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_MUTABLE);


                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,notifTime.getTimeInMillis(), pendingIntent);
            }
        }
    }



}
