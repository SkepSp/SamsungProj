package com.example.daiplan.notification;

import java.io.Serializable;

public class SerActivity implements Serializable {
   private String name;
   private int hours;
   private int minutes;
    public SerActivity(String name, int hours, int minutes) {
        this.name = name;
        this.hours = hours;
        this.minutes = minutes;
    }
    public SerActivity() {}
    public String getName() { return name; }
    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }
}
