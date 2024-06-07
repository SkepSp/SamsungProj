package com.example.daiplan.notification;

import static android.content.Context.ALARM_SERVICE;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.daiplan.list.Activity;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NotificRegister {
    private Context packageContext;
    private final ArrayList <SerActivity>[] activityArrayList = new ArrayList[7];

    public NotificRegister() {}
    //конструктор для вызова в receiver'е
    public NotificRegister(Context context, ArrayList<Activity>[] activityArrayList ) {
        //конструктор для вызова вне receiver'а
        packageContext = context;

        for (int i = 0; i < 7; i++) {
            this.activityArrayList[i] = new ArrayList<SerActivity>();

            for (int j = 0; j < activityArrayList[i].size(); j++) {
                Activity activity = activityArrayList[i].get(j);
                SerActivity serActivity = new SerActivity(activity.name, activity.hourOfStart, activity.minuteOfStart);

                this.activityArrayList[i].add(serActivity);
            }
        }
    }

    public Bundle getNearestInfo(ArrayList <SerActivity>[] activityArrayList) {
        //получаем ближайшее Activity, к моменту вызова функции

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("u");
        int currDayOfWeek = Integer.parseInt(dateFormat.format(new Date())) - 1;

        int nearestDayOfWeekAct = 0;
        SerActivity nearestActivity = new SerActivity();

        boolean flag = false;
        boolean onNextWeek = false;

        for (int i = currDayOfWeek; i < 7 && !flag; i++) {
            for (int j = 0; j < activityArrayList[i].size() && !flag; j++) {
                if (i == currDayOfWeek) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, activityArrayList[i].get(j).getHours());
                    c.set(Calendar.MINUTE, activityArrayList[i].get(j).getMinutes());

                    if (System.currentTimeMillis() < c.getTimeInMillis()) {
                        nearestActivity = activityArrayList[i].get(j);
                        nearestDayOfWeekAct = i;
                        flag = true;
                    }
                    continue;
                }
                nearestActivity = activityArrayList[i].get(j);
                nearestDayOfWeekAct = i;
                flag = true;
            }
        }

        for (int i = 0; i < currDayOfWeek && !flag; i++) {
            for (int j = 0; j < activityArrayList[i].size() && !flag; j++) {
                nearestActivity = activityArrayList[i].get(j);
                nearestDayOfWeekAct = i;
                flag = true;
                onNextWeek = true;
            }
        }

        if (!flag) {
            if (activityArrayList[currDayOfWeek].size() > 0) {
                nearestActivity = activityArrayList[currDayOfWeek].get(0);
                nearestDayOfWeekAct = currDayOfWeek;
                onNextWeek = true;
            }
        }

        //необходимо тк в классе Calendar неделя начинается с SUNDAY
        switch (nearestDayOfWeekAct) {
            case (0):
                nearestDayOfWeekAct = 2; break;
            case (1):
                nearestDayOfWeekAct = 3; break;
            case (2):
                nearestDayOfWeekAct = 4; break;
            case (3):
                nearestDayOfWeekAct = 5; break;
            case (4):
                nearestDayOfWeekAct = 6; break;
            case (5):
                nearestDayOfWeekAct = 7; break;
            case (6):
                nearestDayOfWeekAct = 1; break;
        }

        //необходимая информация для коректной инициализации будильника
        Bundle bundle = new Bundle();
        bundle.putSerializable("activity", nearestActivity);
        bundle.putInt("day", nearestDayOfWeekAct);
        bundle.putBoolean("onNextWeek", onNextWeek);

        return bundle;
    }
    @SuppressLint("ScheduleExactAlarm")
    public void synchronizeNotificaton() {

        Bundle inpBundle = getNearestInfo(activityArrayList);
        SerActivity nearestActivity = (SerActivity) inpBundle.getSerializable("activity");
        int nearestDayOfWeekAct = inpBundle.getInt("day");
        boolean onNextWeek = inpBundle.getBoolean("onNextWeek");

        //тк имя активности у нас инициализируется всегда -> если у активности нет имени, то такой активности в списке нет
        if (nearestActivity.getName() != null) {

            Intent intent = new Intent(packageContext, ReminderBroadcast.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();

            for (int i = 0; i < 7; i++) {
                bundle.putSerializable(String.valueOf(i), activityArrayList[i]);
            }
            bundle.putSerializable("notifyAct", nearestActivity);

            intent.putExtra("all", bundle);

            //устанавливаем время на след будильник
            Calendar notifyTime = Calendar.getInstance();
            notifyTime.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            notifyTime.setFirstDayOfWeek(Calendar.MONDAY);
            notifyTime.set(Calendar.DAY_OF_WEEK, nearestDayOfWeekAct);
            notifyTime.set(Calendar.HOUR_OF_DAY, nearestActivity.getHours());
            notifyTime.set(Calendar.MINUTE, nearestActivity.getMinutes());
            notifyTime.set(Calendar.SECOND, 0);

            if (onNextWeek){
                //добавление одной недели (604800000 - 1 неделя в милисикундах)
                notifyTime.add(Calendar.MILLISECOND, 604800000);
            }

            //отладка
            //System.out.println( "Timer is set to " + notifyTime.getTime().toString());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(packageContext, (int) System.currentTimeMillis(), intent,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);

            AlarmManager alarmManager = (AlarmManager) packageContext.getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime.getTimeInMillis(), pendingIntent);
        } else {
            //отладка
            //System.out.println("Alarm didnt set");
        }
    }
}
