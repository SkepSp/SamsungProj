package com.example.daiplan.notification;


import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.daiplan.R;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class ReminderBroadcast extends BroadcastReceiver {
    NotificationManager notificationManager;
    ArrayList <SerActivity>[] list = new ArrayList[7];

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        assert bundle != null;
        Bundle ext = bundle.getBundle("all");

        for (int i = 0; i < 7; i++) {
            list[i] = (ArrayList<SerActivity>) ext.getSerializable(String.valueOf(i));
        }
        SerActivity notifyActivity = (SerActivity) ext.getSerializable("notifyAct");


        NotificationChannel notificationChannel = new NotificationChannel("notifyReminder", "name", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);


        showNotification(notifyActivity, context);

        Bundle inpBundle = new NotificRegister().getNearestInfo(list);

        SerActivity nearestActivity = (SerActivity) inpBundle.getSerializable("activity");
        int nearestDayOfWeekAct = inpBundle.getInt("day");
        boolean onNextWeek = inpBundle.getBoolean("onNextWeek");

        Calendar notifyTime = Calendar.getInstance();
        notifyTime.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        notifyTime.setFirstDayOfWeek(Calendar.MONDAY);
        notifyTime.set(Calendar.DAY_OF_WEEK, nearestDayOfWeekAct);
        notifyTime.set(Calendar.HOUR_OF_DAY, nearestActivity.getHours());
        notifyTime.set(Calendar.MINUTE, nearestActivity.getMinutes());
        notifyTime.set(Calendar.SECOND, 0);

        if (onNextWeek){
            //add 1 week
            notifyTime.add(Calendar.MILLISECOND, 604800000);
        }



        Intent newIntent = new Intent(context, ReminderBroadcast.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        System.out.println( "(receiver) Timer is set to  " + notifyTime.getTime().toString());

        Bundle newBundle = new Bundle();
        for (int i = 0; i < 7; i++) {
            newBundle.putSerializable(String.valueOf(i), list[i]);
        }
        newBundle.putSerializable("notifyAct", nearestActivity);

        newIntent.putExtra("all", newBundle);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), newIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime.getTimeInMillis(), pendingIntent);
    }

    private void showNotification(SerActivity notifyActivity, Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notifyActivity != null;
        Notification.Builder builder = new Notification.Builder(context, "notifyReminder")
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentTitle(notifyActivity.getName())
                .setContentText("You have smth planned at this time")
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(200, builder.build());
    }
}
