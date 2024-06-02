package com.example.myapplication;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;
import static android.app.NotificationChannel.EDIT_VIBRATION;
import static android.icu.number.NumberRangeFormatter.with;
import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.example.myapplication.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ArrayList <Activity>[] list = new ArrayList[7];

    final String channelID = "channel1";

    final static String CHANNEL_ID = "channel1";
    final static int NOTIFY_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i =0; i<7; i++) {
            list[i] = new ArrayList<>();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createChannelIfNeeded();

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "SetTime");

            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ScheduleExactAlarm")
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ReminderBroadcast.class);


                ArrayList <ParcelableActivity>[] parcelableList = new ArrayList[7];

                Activity activity = new Activity();
                activity.hours = 222;
                list[0].add(activity);

                for (int i =0;i<7;i++) {
                    parcelableList[i] = new ArrayList<>();

                    for (int j = 0; j < list[i].size(); j++) {
                        ParcelableActivity parcelableActivity = new ParcelableActivity();

                        parcelableActivity.name = list[i].get(j).name;
                        parcelableActivity.hours = list[i].get(j).hours;
                        parcelableActivity.minuts = list[i].get(j).minuts;

                        parcelableList[i].add(parcelableActivity);
                    }
                    intent.putExtra("getList" + i, parcelableList[i]);
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
            }
        });

    }

    public void createChannelIfNeeded(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notifyReminder", "name", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}